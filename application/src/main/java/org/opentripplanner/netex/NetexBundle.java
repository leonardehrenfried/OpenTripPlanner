package org.opentripplanner.netex;

import jakarta.xml.bind.JAXBException;
import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import org.opentripplanner.core.model.deduplicator.DeduplicatorService;
import org.opentripplanner.core.model.id.FeedScopedId;
import org.opentripplanner.datastore.api.CompositeDataSource;
import org.opentripplanner.datastore.api.DataSource;
import org.opentripplanner.graph_builder.issue.api.DataImportIssueStore;
import org.opentripplanner.model.impl.TransitDataImportBuilder;
import org.opentripplanner.netex.config.IgnorableFeature;
import org.opentripplanner.netex.config.NetexFeedParameters;
import org.opentripplanner.netex.index.NetexEntityIndex;
import org.opentripplanner.netex.loader.GroupEntries;
import org.opentripplanner.netex.loader.NetexDataSourceHierarchy;
import org.opentripplanner.netex.loader.NetexFileLoader;
import org.opentripplanner.netex.loader.parser.NetexDocumentParser;
import org.opentripplanner.netex.mapping.NetexMapper;
import org.opentripplanner.netex.validation.Validator;
import org.rutebanken.netex.model.PublicationDeliveryStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads/reads a NeTEx bundle of a data source(zip file/directory/cloud storage) and maps it into
 * the OTP internal transit model.
 * <p>
 * The NeTEx loader will use a file naming convention to load files in a particular order and
 * keeping an index of entities to enable linking. The convention is documented here
 * {@link NetexFeedParameters#sharedFilePattern()} and here {@link NetexDataSourceHierarchy}.
 * <p>
 * This class is also responsible for logging progress and exception handling.
 */
public class NetexBundle implements Closeable {

  private static final Logger LOG = LoggerFactory.getLogger(NetexBundle.class);

  /**
   * The number of threads loading files ahead of the mapping, see {@link NetexFileLoader}. Past 4
   * the gain is small, since the mapping on the main thread becomes the bottleneck.
   */
  private static final int LOADER_PARALLELISM = Math.min(
    4,
    Runtime.getRuntime().availableProcessors()
  );

  /**
   * The limits for the files loaded ahead of the mapping. Most files are small, so the workers need
   * many files to stay busy. The size limit bounds the memory used by the parsed documents.
   */
  private static final int LOADER_MAX_FILES_AHEAD = 64;
  private static final long LOADER_MAX_BYTES_AHEAD = 128_000_000;

  private final CompositeDataSource source;

  private final NetexDataSourceHierarchy hierarchy;
  private final TransitDataImportBuilder transitBuilder;

  private final String feedId;
  private final Set<String> ferryIdsNotAllowedForBicycle;
  private final Collection<FeedScopedId> routeToCentroidStopPlaceIds;
  private final double maxStopToShapeSnapDistance;
  private final double transitShapeSimplificationToleranceMeters;
  private final boolean noTransfersOnIsolatedStops;
  private final Set<IgnorableFeature> ignoredFeatures;
  /** The NeTEx entities loaded from the input files and passed on to the mapper. */
  private NetexEntityIndex index = new NetexEntityIndex();
  /** Report errors to issue store */
  private DataImportIssueStore issueStore;
  /** maps the NeTEx XML document to OTP transit model. */
  private NetexMapper mapper;
  private NetexFileLoader fileLoader;

  public NetexBundle(
    String feedId,
    CompositeDataSource source,
    NetexDataSourceHierarchy hierarchy,
    TransitDataImportBuilder transitBuilder,
    Set<String> ferryIdsNotAllowedForBicycle,
    Collection<FeedScopedId> routeToCentroidStopPlaceIds,
    double maxStopToShapeSnapDistance,
    double transitShapeSimplificationToleranceMeters,
    boolean noTransfersOnIsolatedStops,
    Set<IgnorableFeature> ignorableFeatures
  ) {
    this.feedId = feedId;
    this.source = source;
    this.hierarchy = hierarchy;
    this.transitBuilder = transitBuilder;
    this.ferryIdsNotAllowedForBicycle = ferryIdsNotAllowedForBicycle;
    this.routeToCentroidStopPlaceIds = Set.copyOf(routeToCentroidStopPlaceIds);
    this.maxStopToShapeSnapDistance = maxStopToShapeSnapDistance;
    this.transitShapeSimplificationToleranceMeters = transitShapeSimplificationToleranceMeters;
    this.noTransfersOnIsolatedStops = noTransfersOnIsolatedStops;
    this.ignoredFeatures = Set.copyOf(ignorableFeatures);
  }

  /** load the bundle, map it to the OTP transit model and return */
  public TransitDataImportBuilder loadBundle(
    DeduplicatorService deduplicator,
    DataImportIssueStore issueStore
  ) {
    LOG.info("Reading {}", hierarchy.description());

    this.issueStore = issueStore;

    // init mapper
    mapper = new NetexMapper(
      transitBuilder,
      feedId,
      deduplicator,
      issueStore,
      ferryIdsNotAllowedForBicycle,
      routeToCentroidStopPlaceIds,
      maxStopToShapeSnapDistance,
      transitShapeSimplificationToleranceMeters,
      noTransfersOnIsolatedStops
    );

    // Load data
    loadFileEntries();

    return transitBuilder;
  }

  public void checkInputs() {
    if (!source.exists()) {
      throw new RuntimeException("NeTEx " + source.path() + " does not exist.");
    }
  }

  /* private methods */

  @Override
  public void close() throws IOException {
    source.close();
  }

  /** Load all files entries in the bundle */
  private void loadFileEntries() {
    try (
      var loader = new NetexFileLoader(
        hierarchy,
        ignoredFeatures,
        LOADER_PARALLELISM,
        LOADER_MAX_FILES_AHEAD,
        LOADER_MAX_BYTES_AHEAD
      )
    ) {
      fileLoader = loader;
      loadFileEntriesInOrder();
    } finally {
      fileLoader = null;
    }
    mapper.finishUp();
    NetexDocumentParser.finishUp();
  }

  private void loadFileEntriesInOrder() {
    // Load global shared files
    loadSharedFiles("shared file", hierarchy.sharedEntries());
    validateIndex();
    mapIndexToTransitRepository();

    for (GroupEntries group : hierarchy.groups()) {
      LOG.info("reading group {}", group.name());

      scopeInputData(() -> {
        // Load shared group files
        loadSharedFiles("shared group file", group.sharedEntries());
        validateIndex();
        // The independent files in the group are loaded and validated in parallel from now on
        fileLoader.groupIndexReady(group, index);
        mapIndexToTransitRepository();

        for (DataSource entry : group.independentEntries()) {
          var loaded = loadGroupFile(entry);
          loaded.issues().forEach(issueStore::add);
          scopeLoadedInputData(loaded.index(), this::mapIndexToTransitRepository);
        }
      });
    }
  }

  /**
   * make a new index and pushes it on the index stack, before executing the task and at the end pop
   * of the index.
   */
  private void scopeInputData(Runnable task) {
    scopeLoadedInputData(index.push(), task);
  }

  /**
   * Same as {@link #scopeInputData(Runnable)}, but with a new index that is already created as a
   * child of the current index.
   */
  private void scopeLoadedInputData(NetexEntityIndex childIndex, Runnable task) {
    if (childIndex.pop() != index) {
      throw new IllegalStateException("The index is not a child of the current index");
    }
    index = childIndex;
    mapper = mapper.push();
    task.run();
    mapper = mapper.pop();
    index = index.pop();
  }

  /**
   * Load a set of files into the current index. The entries are mapped to the OTP Transit model
   * after the loading is complete. It is important to do this in 2 steps to be able to link
   * references. An attempt to map each entry, when read, would lead to missing references, since
   * the order entries are read is not enforced in any way.
   */
  private void loadSharedFiles(String fileDescription, Iterable<DataSource> entries) {
    for (DataSource entry : entries) {
      try {
        LOG.info("reading entity {}: {}", fileDescription, entry.name());
        PublicationDeliveryStructure doc = fileLoader.nextSharedFile(entry);
        NetexDocumentParser.parseAndPopulateIndex(index, doc, ignoredFeatures);
      } catch (JAXBException e) {
        throw new RuntimeException(e.getMessage(), e);
      }
    }
  }

  /** Load an independent group file into its own level of the index, and validate it. */
  private NetexFileLoader.LoadedGroupFile loadGroupFile(DataSource entry) {
    try {
      LOG.info("reading entity group file: {}", entry.name());
      return fileLoader.nextGroupFile(entry);
    } catch (JAXBException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /** Validate input data, and remove invalid data */
  private void validateIndex() {
    Validator.validate(index, issueStore::add);
  }

  /** map current NeTEx objects into the OTP Transit Model */
  private void mapIndexToTransitRepository() {
    mapper.mapNetexToOtp(index.readOnlyView());
  }
}
