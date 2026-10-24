package org.opentripplanner.netex.loader;

import jakarta.xml.bind.JAXBException;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.opentripplanner.datastore.api.DataSource;
import org.opentripplanner.graph_builder.issue.api.DataImportIssue;
import org.opentripplanner.netex.config.IgnorableFeature;
import org.opentripplanner.netex.index.NetexEntityIndex;
import org.opentripplanner.netex.loader.parser.NetexDocumentParser;
import org.opentripplanner.netex.validation.Validator;
import org.rutebanken.netex.model.PublicationDeliveryStructure;

/**
 * Loads the files of a {@link NetexDataSourceHierarchy} on a pool of worker threads, ahead of the
 * main thread consuming them.
 * <p>
 * Parsing the XML is the most expensive part of loading NeTEx, and it is independent for each
 * file. The independent files of a group can also be loaded into the entity index and validated
 * independently: each is loaded into its own level of the index, with the complete index of the
 * group as the parent level. The parent level is only read while doing so.
 * <p>
 * Loading the shared files into the index, and mapping the index to the OTP model, depend on the
 * order the files are loaded in, so these are still done in sequence by the caller:
 * <ol>
 *   <li>The caller requests the parsed documents of the shared files with
 *   {@link #nextSharedFile}, and loads them into the index.</li>
 *   <li>When all shared files of a group are loaded and validated, the caller passes the index of
 *   the group to {@link #groupIndexReady}. Now the independent files of the group are loaded.</li>
 *   <li>The caller requests the loaded index of each independent file with
 *   {@link #nextGroupFile}, and maps it.</li>
 * </ol>
 * The caller must request the entries in the order of the hierarchy: the shared files, then for
 * each group, the shared files of the group followed by the independent files.
 * <p>
 * Parsed documents use a lot more memory than the XML files, so the number of files loaded ahead
 * of the one being consumed is limited by both the number of files and their total size. The
 * workers need many files ahead to stay busy, since most files are small and the group files of a
 * group can only be loaded after the shared files of the group are consumed.
 */
public class NetexFileLoader implements Closeable {

  private final Set<IgnorableFeature> ignoredFeatures;
  private final int maxFilesAhead;
  private final long maxBytesAhead;
  private final ExecutorService executor;
  private final Map<GroupEntries, CompletableFuture<NetexEntityIndex>> groupIndexes =
    new IdentityHashMap<>();
  private final Deque<PlannedFile> plannedFiles;
  private final Deque<StartedFile> startedFiles = new ArrayDeque<>();
  private long bytesAhead = 0;

  /**
   * @param parallelism the number of worker threads.
   * @param maxFilesAhead the maximum number of files loaded ahead of the one being consumed.
   * @param maxBytesAhead the maximum total size of the XML files loaded ahead of the one being
   *                      consumed. A single file bigger than this is still loaded, but alone.
   */
  public NetexFileLoader(
    NetexDataSourceHierarchy hierarchy,
    Set<IgnorableFeature> ignoredFeatures,
    int parallelism,
    int maxFilesAhead,
    long maxBytesAhead
  ) {
    if (parallelism < 1 || maxFilesAhead < 1) {
      throw new IllegalArgumentException(
        "parallelism and maxFilesAhead must be at least 1: %d, %d".formatted(
          parallelism,
          maxFilesAhead
        )
      );
    }
    this.ignoredFeatures = Set.copyOf(ignoredFeatures);
    this.maxFilesAhead = maxFilesAhead;
    this.maxBytesAhead = maxBytesAhead;
    this.executor = Executors.newFixedThreadPool(parallelism, new LoaderThreadFactory());
    this.plannedFiles = new ArrayDeque<>(planFiles(hierarchy));
  }

  /**
   * Return the parsed document of the given shared file, waiting for it to be parsed if
   * necessary.
   *
   * @throws IllegalStateException if the entry is not the next one in the hierarchy, or not a
   *                               shared file.
   */
  public PublicationDeliveryStructure nextSharedFile(DataSource entry) throws JAXBException {
    return next(entry, PublicationDeliveryStructure.class);
  }

  /**
   * Notify the loader that all shared files of the given group are loaded into the given index and
   * validated. The independent files of the group are loaded into child levels of this index, so
   * it must not be changed until all independent files of the group are consumed.
   */
  public void groupIndexReady(GroupEntries group, NetexEntityIndex groupIndex) {
    var future = groupIndexes.get(group);
    if (future == null) {
      throw new IllegalArgumentException("Unknown NeTEx group: " + group.name());
    }
    future.complete(groupIndex);
  }

  /**
   * Return the loaded and validated index of the given independent group file, waiting for it to
   * be loaded if necessary. The returned index is a child level of the group index, see
   * {@link #groupIndexReady}.
   *
   * @throws IllegalStateException if the entry is not the next one in the hierarchy, or not an
   *                               independent group file, or the index of its group is not ready.
   */
  public LoadedGroupFile nextGroupFile(DataSource entry) throws JAXBException {
    return next(entry, LoadedGroupFile.class);
  }

  @Override
  public void close() {
    executor.shutdownNow();
    startedFiles.clear();
  }

  private List<PlannedFile> planFiles(NetexDataSourceHierarchy hierarchy) {
    List<PlannedFile> files = new ArrayList<>();
    hierarchy.sharedEntries().forEach(entry -> files.add(planSharedFile(entry)));

    for (GroupEntries group : hierarchy.groups()) {
      var groupIndex = new CompletableFuture<NetexEntityIndex>();
      groupIndexes.put(group, groupIndex);
      group.sharedEntries().forEach(entry -> files.add(planSharedFile(entry)));
      group.independentEntries().forEach(entry -> files.add(planGroupFile(entry, groupIndex)));
    }
    return files;
  }

  private PlannedFile planSharedFile(DataSource entry) {
    return new PlannedFile(entry, null, () ->
      CompletableFuture.supplyAsync(() -> parse(entry), executor)
    );
  }

  private PlannedFile planGroupFile(
    DataSource entry,
    CompletableFuture<NetexEntityIndex> groupIndex
  ) {
    return new PlannedFile(entry, groupIndex, () ->
      CompletableFuture.supplyAsync(() -> parse(entry), executor).thenCombineAsync(
        groupIndex,
        this::loadGroupFile,
        executor
      )
    );
  }

  private PublicationDeliveryStructure parse(DataSource entry) {
    try (InputStream stream = entry.asInputStream()) {
      // The parser is not thread-safe, but cheap to create since the JAXB context is shared
      return new NetexXmlParser().parseXmlDoc(stream);
    } catch (JAXBException e) {
      throw new CompletionException(e);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private LoadedGroupFile loadGroupFile(
    PublicationDeliveryStructure doc,
    NetexEntityIndex groupIndex
  ) {
    var index = groupIndex.push();
    NetexDocumentParser.parseAndPopulateIndex(index, doc, ignoredFeatures);
    // The issues are reported by the caller, to keep them in the same order as the files
    List<DataImportIssue> issues = new ArrayList<>();
    Validator.validate(index, issues::add);
    return new LoadedGroupFile(index, issues);
  }

  private <T> T next(DataSource entry, Class<T> type) throws JAXBException {
    startAhead();
    var next = startedFiles.poll();
    if (next != null) {
      bytesAhead -= size(next.entry());
    }
    if (next == null || next.entry() != entry) {
      throw new IllegalStateException(
        "NeTEx entry %s requested out of order, expected: %s".formatted(
          entry.path(),
          next == null ? "none" : next.entry().path()
        )
      );
    }
    boolean isGroupFile = next.groupIndex() != null;
    if (isGroupFile != (type == LoadedGroupFile.class)) {
      throw new IllegalStateException(
        "NeTEx entry %s is %s".formatted(
          entry.path(),
          isGroupFile ? "an independent group file" : "a shared file"
        )
      );
    }
    if (isGroupFile && !next.groupIndex().isDone()) {
      // Waiting for the file would never return
      throw new IllegalStateException("The group index is not ready for: " + entry.path());
    }
    // Keep the workers busy while the caller processes this file
    startAhead();
    return type.cast(await(next.result()));
  }

  private void startAhead() {
    while (!plannedFiles.isEmpty() && startedFiles.size() < maxFilesAhead) {
      long size = size(plannedFiles.peek().entry());
      if (!startedFiles.isEmpty() && bytesAhead + size > maxBytesAhead) {
        return;
      }
      var file = plannedFiles.poll();
      bytesAhead += size;
      startedFiles.add(new StartedFile(file.entry(), file.groupIndex(), file.start().get()));
    }
  }

  /** The size of the XML file, or 0 if unknown */
  private static long size(DataSource entry) {
    return Math.max(entry.size(), 0);
  }

  private static Object await(CompletableFuture<?> result) throws JAXBException {
    try {
      return result.get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Interrupted while loading NeTEx", e);
    } catch (ExecutionException e) {
      switch (e.getCause()) {
        case JAXBException jaxbException -> throw jaxbException;
        case RuntimeException runtimeException -> throw runtimeException;
        case Error error -> throw error;
        default -> throw new RuntimeException(e.getCause());
      }
    }
  }

  /**
   * An independent group file loaded into its own level of the index, and validated.
   *
   * @param issues the issues found when validating, which are not yet reported.
   */
  public record LoadedGroupFile(NetexEntityIndex index, List<DataImportIssue> issues) {}

  /**
   * @param groupIndex the index of the group for independent group files, {@code null} for shared
   *                   files.
   */
  private record PlannedFile(
    DataSource entry,
    @Nullable CompletableFuture<NetexEntityIndex> groupIndex,
    Supplier<CompletableFuture<?>> start
  ) {}

  private record StartedFile(
    DataSource entry,
    @Nullable CompletableFuture<NetexEntityIndex> groupIndex,
    CompletableFuture<?> result
  ) {}

  private static class LoaderThreadFactory implements ThreadFactory {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public Thread newThread(Runnable task) {
      var thread = new Thread(task, "netex-loader-" + counter.incrementAndGet());
      thread.setDaemon(true);
      return thread;
    }
  }
}
