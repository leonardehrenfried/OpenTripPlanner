package org.opentripplanner.osm;

import crosby.binary.Osmformat;
import gnu.trove.list.TByteList;
import gnu.trove.list.array.TByteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tracks which primitive kind(s) (nodes/ways/relations) each "OSMData" block of a PBF file
 * actually contains, so that {@link OsmParser} - which reads the file once per
 * {@link OsmParserPhase} - can skip decompressing, on its second and third reads, blocks that
 * cannot contain anything relevant to the current phase.
 * <p>
 * The PBF spec guarantees that an {@link Osmformat.PrimitiveGroup} only contains a single
 * primitive type, but not that a whole block does. The first read has to fully decode every
 * block anyway, so this class records what was actually observed in each block, in file order,
 * during that read. Because the recording reflects what was actually observed - not an
 * assumption of one-type-per-block - a block that mixes several primitive types is simply never
 * skipped for any phase it's relevant to; no separate handling of "mixed" blocks is needed.
 */
class BlockKindIndex {

  private static final Logger LOG = LoggerFactory.getLogger(BlockKindIndex.class);

  // Only 3 bits are ever used, so the per-block mask is stored as a byte rather than an int.
  private static final byte KIND_RELATIONS = 1 << 0;
  private static final byte KIND_WAYS = 1 << 1;
  private static final byte KIND_NODES = 1 << 2;

  private final TByteList blockKinds = new TByteArrayList();
  private int phaseCount = 0;
  private int blockCursor;
  private int skippedBlocks;
  private int totalDataBlocks;

  /** Call at the start of every phase, before any block of that phase is seen. */
  void startPhase() {
    phaseCount++;
    blockCursor = 0;
    skippedBlocks = 0;
    totalDataBlocks = 0;
  }

  /** The first phase has to decode every block to find out what's in it. */
  private boolean isIndexingPhase() {
    return phaseCount == 1;
  }

  /**
   * Whether an "OSMData" block can be skipped (its compressed body left undecoded) because it
   * was already found, during the indexing phase, to contain nothing relevant to {@code phase}.
   */
  boolean shouldSkip(OsmParserPhase phase) {
    totalDataBlocks++;
    if (isIndexingPhase()) {
      return false;
    }

    int cursor = blockCursor++;
    if (cursor >= blockKinds.size()) {
      // The recorded index is shorter than the file (eg. the indexing phase aborted early)
      // - fail safe and decode the block rather than risk skipping real data.
      return false;
    }

    boolean skip = (blockKinds.get(cursor) & kindOf(phase)) == 0;
    if (skip) {
      skippedBlocks++;
    }
    return skip;
  }

  /** Record which primitive kind(s) the block contains, if this is still the indexing phase. */
  void recordIfIndexing(Osmformat.PrimitiveBlock block) {
    if (isIndexingPhase()) {
      blockKinds.add(kindsPresentIn(block));
    }
  }

  /** Log a summary of how many blocks were skipped during the phase that just completed. */
  void logSummary(OsmParserPhase phase) {
    if (totalDataBlocks > 0) {
      LOG.info(
        "OSM {} phase: skipped {}/{} blocks that could not contain relevant data",
        phase,
        skippedBlocks,
        totalDataBlocks
      );
    }
  }

  private static byte kindsPresentIn(Osmformat.PrimitiveBlock block) {
    byte kinds = 0;
    for (Osmformat.PrimitiveGroup group : block.getPrimitivegroupList()) {
      if (group.hasDense() || group.getNodesCount() > 0) {
        kinds |= KIND_NODES;
      }
      if (group.getWaysCount() > 0) {
        kinds |= KIND_WAYS;
      }
      if (group.getRelationsCount() > 0) {
        kinds |= KIND_RELATIONS;
      }
    }
    return kinds;
  }

  private static byte kindOf(OsmParserPhase phase) {
    return switch (phase) {
      case Relations -> KIND_RELATIONS;
      case Ways -> KIND_WAYS;
      case Nodes -> KIND_NODES;
    };
  }
}
