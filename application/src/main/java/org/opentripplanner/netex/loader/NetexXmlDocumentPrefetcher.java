package org.opentripplanner.netex.loader;

import jakarta.xml.bind.JAXBException;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.opentripplanner.datastore.api.DataSource;
import org.rutebanken.netex.model.PublicationDeliveryStructure;

/**
 * Parses NeTEx XML documents on a pool of worker threads, ahead of the main thread consuming
 * them.
 * <p>
 * Parsing the XML is the most expensive part of loading NeTEx, but it is independent for each
 * file. Populating the entity index, validating and mapping depend on the order the files are
 * loaded in, so they are still done in sequence by the caller. The caller must request the
 * documents in the same order as the entries are passed in to the constructor.
 * <p>
 * Parsed documents use a lot more memory than the XML files, so at most {@code parallelism}
 * documents are parsed ahead of the one being consumed.
 */
public class NetexXmlDocumentPrefetcher implements Closeable {

  private final Iterator<DataSource> entries;
  private final int maxDocumentsAhead;
  private final ExecutorService executor;
  private final Deque<PendingDocument> pending = new ArrayDeque<>();

  /**
   * @param entries all entries to parse, in the order they are requested with {@link #next}.
   * @param parallelism the number of worker threads and documents parsed ahead.
   */
  public NetexXmlDocumentPrefetcher(List<DataSource> entries, int parallelism) {
    if (parallelism < 1) {
      throw new IllegalArgumentException("parallelism must be at least 1: " + parallelism);
    }
    this.entries = List.copyOf(entries).iterator();
    this.maxDocumentsAhead = parallelism;
    this.executor = Executors.newFixedThreadPool(parallelism, new ParserThreadFactory());
  }

  /**
   * Return the parsed document for the given entry, waiting for it to be parsed if necessary.
   *
   * @throws IllegalStateException if the entry is not the next one in the order given in the
   *                               constructor.
   */
  public PublicationDeliveryStructure next(DataSource entry) throws JAXBException {
    submitAhead();
    var next = pending.poll();
    if (next == null || next.entry() != entry) {
      throw new IllegalStateException(
        "NeTEx entry %s requested out of order, expected: %s".formatted(
          entry.path(),
          next == null ? "none" : next.entry().path()
        )
      );
    }
    // Keep the workers busy while the caller processes this document
    submitAhead();
    return await(next.document());
  }

  @Override
  public void close() {
    executor.shutdownNow();
    pending.clear();
  }

  private void submitAhead() {
    while (pending.size() < maxDocumentsAhead && entries.hasNext()) {
      var entry = entries.next();
      pending.add(new PendingDocument(entry, executor.submit(() -> parse(entry))));
    }
  }

  private PublicationDeliveryStructure parse(DataSource entry) throws JAXBException {
    try (InputStream stream = entry.asInputStream()) {
      // The parser is not thread-safe, but cheap to create since the JAXB context is shared
      return new NetexXmlParser().parseXmlDoc(stream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static PublicationDeliveryStructure await(Future<PublicationDeliveryStructure> document)
    throws JAXBException {
    try {
      return document.get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Interrupted while parsing NeTEx XML", e);
    } catch (ExecutionException e) {
      switch (e.getCause()) {
        case JAXBException jaxbException -> throw jaxbException;
        case RuntimeException runtimeException -> throw runtimeException;
        case Error error -> throw error;
        default -> throw new RuntimeException(e.getCause());
      }
    }
  }

  private record PendingDocument(DataSource entry, Future<PublicationDeliveryStructure> document) {}

  private static class ParserThreadFactory implements ThreadFactory {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public Thread newThread(Runnable task) {
      var thread = new Thread(task, "netex-xml-parser-" + counter.incrementAndGet());
      thread.setDaemon(true);
      return thread;
    }
  }
}
