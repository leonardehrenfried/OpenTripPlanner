package org.opentripplanner.osm;

import crosby.binary.Fileformat;
import crosby.binary.Osmformat;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.IntPredicate;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Reads an OSM PBF file, decompressing <em>and</em> protobuf-parsing the file blocks on a thread
 * pool, while handing the parsed blocks to the {@link OsmParser} sequentially, in file order, on
 * the calling thread.
 * <p>
 * The osmpbf library's {@code ParallelBlockInputStream} only decompresses in parallel and leaves
 * the (more expensive) protobuf parsing to the calling thread.
 */
class ParallelPbfReader {

  private static final String OSM_HEADER = "OSMHeader";
  private static final String OSM_DATA = "OSMData";

  private final InputStream input;
  private final ExecutorService executor;
  private final int maxInFlight;

  ParallelPbfReader(InputStream input, ExecutorService executor, int parallelism) {
    this.input = input;
    this.executor = executor;
    this.maxInFlight = 2 * parallelism;
  }

  /**
   * @param skipDataBlock tested with the index of each data block in the file; blocks for which
   *                      it returns true are skipped without being decompressed or parsed.
   */
  void process(OsmParser parser, IntPredicate skipDataBlock) throws IOException {
    var in = new DataInputStream(new BufferedInputStream(input, 1 << 16));
    var queue = new ArrayDeque<Future<DecodedBlock>>();
    int index = 0;
    while (true) {
      int headerSize;
      try {
        headerSize = in.readInt();
      } catch (EOFException e) {
        break;
      }
      var header = Fileformat.BlobHeader.parseFrom(in.readNBytes(headerSize));
      int blockIndex = index++;
      String type = header.getType();
      if (OSM_DATA.equals(type) && skipDataBlock.test(blockIndex)) {
        in.skipNBytes(header.getDatasize());
        continue;
      }
      byte[] blob = in.readNBytes(header.getDatasize());
      queue.add(executor.submit(() -> decode(blockIndex, type, blob)));
      while (queue.size() >= maxInFlight) {
        handle(parser, take(queue));
      }
    }
    while (!queue.isEmpty()) {
      handle(parser, take(queue));
    }
    parser.complete();
  }

  private static void handle(OsmParser parser, DecodedBlock block) {
    if (block.header() != null) {
      parser.parse(block.header());
    } else if (block.data() != null) {
      parser.parse(block.index(), block.data());
    }
  }

  private static DecodedBlock take(ArrayDeque<Future<DecodedBlock>> queue) throws IOException {
    try {
      return queue.poll().get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IOException(e);
    } catch (ExecutionException e) {
      throw new IOException(e.getCause());
    }
  }

  private static DecodedBlock decode(int index, String type, byte[] blobBytes) throws Exception {
    var blob = Fileformat.Blob.parseFrom(blobBytes);
    byte[] raw;
    if (blob.hasRaw()) {
      raw = blob.getRaw().toByteArray();
    } else if (blob.hasZlibData()) {
      raw = inflate(blob);
    } else {
      throw new IOException("Unsupported PBF blob compression: " + blob.getDataCase());
    }
    return switch (type) {
      case OSM_HEADER -> new DecodedBlock(index, Osmformat.HeaderBlock.parseFrom(raw), null);
      case OSM_DATA -> new DecodedBlock(index, null, Osmformat.PrimitiveBlock.parseFrom(raw));
      default -> new DecodedBlock(index, null, null);
    };
  }

  private static byte[] inflate(Fileformat.Blob blob) throws DataFormatException, IOException {
    byte[] raw = new byte[blob.getRawSize()];
    var inflater = new Inflater();
    try {
      inflater.setInput(blob.getZlibData().asReadOnlyByteBuffer());
      int n = inflater.inflate(raw);
      if (n != raw.length || !inflater.finished()) {
        throw new IOException("Corrupt PBF blob: expected " + raw.length + " bytes, got " + n);
      }
    } finally {
      inflater.end();
    }
    return raw;
  }

  private record DecodedBlock(
    int index,
    Osmformat.HeaderBlock header,
    Osmformat.PrimitiveBlock data
  ) {}
}
