package org.opentripplanner.graph_builder.module.osm;

import java.util.Collection;
import java.util.Set;
import org.opentripplanner.graph_builder.issue.api.DataImportIssueStore;
import org.opentripplanner.graph_builder.module.osm.parameters.OsmProcessingParameters;
import org.opentripplanner.graph_builder.services.osm.CustomNamer;
import org.opentripplanner.openstreetmap.OsmProvider;
import org.opentripplanner.routing.graph.Graph;

/**
 * Builder for the {@link OsmModule}
 */
public class OsmModuleBuilder {

  private final Collection<OsmProvider> providers;
  private final Graph graph;
  private Set<String> boardingAreaRefTags = Set.of();
  private DataImportIssueStore issueStore = DataImportIssueStore.NOOP;
  private CustomNamer customNamer;
  private boolean areaVisibility = false;
  private boolean platformEntriesLinking = false;
  private boolean staticParkAndRide = false;
  private boolean staticBikeParkAndRide = false;
  private boolean banDiscouragedWalking = false;
  private boolean banDiscouragedBiking = false;
  private int maxAreaNodes;

  OsmModuleBuilder(Collection<OsmProvider> providers, Graph graph) {
    this.providers = providers;
    this.graph = graph;
  }

  public OsmModuleBuilder withBoardingAreaRefTags(Set<String> boardingAreaRefTags) {
    this.boardingAreaRefTags = boardingAreaRefTags;
    return this;
  }

  public OsmModuleBuilder withIssueStore(DataImportIssueStore issueStore) {
    this.issueStore = issueStore;
    return this;
  }

  public OsmModuleBuilder withCustomNamer(CustomNamer customNamer) {
    this.customNamer = customNamer;
    return this;
  }

  public OsmModuleBuilder withAreaVisibility(boolean areaVisibility) {
    this.areaVisibility = areaVisibility;
    return this;
  }

  public OsmModuleBuilder withPlatformEntriesLinking(boolean platformEntriesLinking) {
    this.platformEntriesLinking = platformEntriesLinking;
    return this;
  }

  public OsmModuleBuilder withStaticParkAndRide(boolean staticParkAndRide) {
    this.staticParkAndRide = staticParkAndRide;
    return this;
  }

  public OsmModuleBuilder withStaticBikeParkAndRide(boolean staticBikeParkAndRide) {
    this.staticBikeParkAndRide = staticBikeParkAndRide;
    return this;
  }

  public OsmModuleBuilder withBanDiscouragedWalking(boolean banDiscouragedWalking) {
    this.banDiscouragedWalking = banDiscouragedWalking;
    return this;
  }

  public OsmModuleBuilder withBanDiscouragedBiking(boolean banDiscouragedBiking) {
    this.banDiscouragedBiking = banDiscouragedBiking;
    return this;
  }

  public OsmModuleBuilder withMaxAreaNodes(int maxAreaNodes) {
    this.maxAreaNodes = maxAreaNodes;
    return this;
  }

  public OsmModule build() {
    return new OsmModule(
      providers,
      graph,
      issueStore,
      new OsmProcessingParameters(
        boardingAreaRefTags,
        customNamer,
        maxAreaNodes,
        areaVisibility,
        platformEntriesLinking,
        staticParkAndRide,
        staticBikeParkAndRide,
        banDiscouragedWalking,
        banDiscouragedBiking
      )
    );
  }
}
