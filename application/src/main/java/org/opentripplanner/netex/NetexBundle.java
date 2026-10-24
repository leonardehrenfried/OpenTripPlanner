package org.opentripplanner.netex;

import jakarta.xml.bind.JAXBException;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import org.opentripplanner.netex.loader.NetexXmlDocumentPrefetcher;
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
   * The number of threads parsing XML files ahead of the loading, see
   * {@link NetexXmlDocumentPrefetcher}. Past 4 the gain is small, since the loading on the main
   * thread becomes the bottleneck, while more parsed documents are kept in memory.
   */
  private static final int XML_PARSE_PARALLELISM = Math.min(
    4,
    Runtime.getRuntime().availableProcessors()
  );

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
  private NetexXmlDocumentPrefetcher xmlParser;

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
    try (var parser = new NetexXmlDocumentPrefetcher(entriesInLoadOrder(), XML_PARSE_PARALLELISM)) {
      xmlParser = parser;
      loadFileEntriesInOrder();
    } finally {
      xmlParser = null;
    }
    mapper.finishUp();
    NetexDocumentParser.finishUp();
  }

  /**
   * All entries in the order they are loaded by {@link #loadFileEntriesInOrder()}, this is the
   * order the XML documents are parsed in.
   */
  private List<DataSource> entriesInLoadOrder() {
    List<DataSource> entries = new ArrayList<>();
    hierarchy.sharedEntries().forEach(entries::add);
    for (GroupEntries group : hierarchy.groups()) {
      entries.addAll(group.sharedEntries());
      entries.addAll(group.independentEntries());
    }
    return entries;
  }

  private void loadFileEntriesInOrder() {
    // Load global shared files
    loadFilesThenMapToTransitRepository("shared file", hierarchy.sharedEntries());

    for (GroupEntries group : hierarchy.groups()) {
      LOG.info("reading group {}", group.name());

      scopeInputData(() -> {
        // Load shared group files
        loadFilesThenMapToTransitRepository("shared group file", group.sharedEntries());

        for (DataSource entry : group.independentEntries()) {
          scopeInputData(() -> {
            // Load each independent file in group
            loadFilesThenMapToTransitRepository("group file", List.of(entry));
          });
        }
      });
    }
  }

  /**
   * make a new index and pushes it on the index stack, before executing the task and at the end pop
   * of the index.
   */
  private void scopeInputData(Runnable task) {
    index = index.push();
    mapper = mapper.push();
    task.run();
    mapper = mapper.pop();
    index = index.pop();
  }

  /**
   * Load a set of files and map the entries to OTP Transit model after the loading is complete. It
   * is important to do this in 2 steps to be able to link references. An attempt to map each entry,
   * when read, would lead to missing references, since the order entries are read is not enforced
   * in any way.
   */
  private void loadFilesThenMapToTransitRepository(
    String fileDescription,
    Iterable<DataSource> entries
  ) {
    for (DataSource entry : entries) {
      // Load entry and store it in the index
      loadSingeFileEntry(fileDescription, entry);
    }

    // Validate input data, and remove invalid data
    Validator.validate(index, issueStore);

    // map current NeTEx objects into the OTP Transit Model
    mapper.mapNetexToOtp(index.readOnlyView());
  }

  /** Load a single entry and store it in the index for later */
  private void loadSingeFileEntry(String fileDescription, DataSource entry) {
    try {
      LOG.info("reading entity {}: {}", fileDescription, entry.name());
      PublicationDeliveryStructure doc = xmlParser.next(entry);
      NetexDocumentParser.parseAndPopulateIndex(index, doc, ignoredFeatures);
    } catch (JAXBException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
