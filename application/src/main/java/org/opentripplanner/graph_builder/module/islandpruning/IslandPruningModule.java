package org.opentripplanner.graph_builder.module.islandpruning;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.opentripplanner.graph_builder.issue.api.DataImportIssueStore;
import org.opentripplanner.graph_builder.issues.GraphConnectivity;
import org.opentripplanner.graph_builder.model.GraphBuilderModule;
import org.opentripplanner.graph_builder.module.StreetLinkerModule;
import org.opentripplanner.street.graph.Graph;
import org.opentripplanner.street.model.StreetTraversalPermission;
import org.opentripplanner.street.model.edge.AreaEdge;
import org.opentripplanner.street.model.edge.AreaGroup;
import org.opentripplanner.street.model.edge.Edge;
import org.opentripplanner.street.model.edge.StreetEdge;
import org.opentripplanner.street.model.vertex.StreetVertex;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.model.vertex.VertexLabel;
import org.opentripplanner.street.search.TraverseMode;
import org.opentripplanner.transit.service.TransitRepository;
import org.opentripplanner.utils.time.DurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This module is part of the {@link GraphBuilderModule} process. It extends the functionality of
 * PruneFloatingIslands by considering also through traffic limitations. It is quite common that no
 * thru edges break connectivity of the graph, creating islands. The quality of the graph can be
 * improved by converting such islands to nothru state so that routing can start / end from such an
 * island.
 */
public class IslandPruningModule implements GraphBuilderModule {

  private static final Logger LOG = LoggerFactory.getLogger(IslandPruningModule.class);

  private final Graph graph;
  private final TransitRepository transitRepository;
  private final DataImportIssueStore issueStore;

  @Nullable
  private final StreetLinkerModule streetLinkerModule;

  private final IslandPruningParameters parameters;
  private final IslandFinder islandFinder;

  public IslandPruningModule(
    Graph graph,
    TransitRepository transitRepository,
    DataImportIssueStore issueStore,
    @Nullable StreetLinkerModule streetLinkerModule,
    IslandPruningParameters parameters
  ) {
    this.graph = graph;
    this.transitRepository = transitRepository;
    this.issueStore = issueStore;
    this.streetLinkerModule = streetLinkerModule;
    this.parameters = parameters;
    this.islandFinder = new IslandFinder(graph, parameters);
  }

  @Override
  public void buildGraph() {
    Instant start = Instant.now();

    LOG.info(
      "Threshold with stops {}, without stops {}, adaptive coeff {} and distance {}",
      parameters.pruningThresholdIslandWithStops(),
      parameters.pruningThresholdIslandWithoutStops(),
      parameters.adaptivePruningFactor(),
      parameters.adaptivePruningDistance()
    );

    // The computation of the islands for each mode only reads the graph, so the three modes
    // can safely be computed in parallel.
    var modes = List.of(TraverseMode.BICYCLE, TraverseMode.WALK, TraverseMode.CAR);
    var computations = modes.parallelStream().map(islandFinder::computeIslands).toList();
    // Applying the pruning decisions mutates shared edges and vertices, so that part is kept
    // strictly sequential, in the same order as before.
    for (IslandComputation computation : computations) {
      applyIslandPruning(computation);
    }

    // reconnect stops that got disconnected
    if (streetLinkerModule != null) {
      LOG.info("Reconnecting stops");
      streetLinkerModule.linkTransitStops(graph, transitRepository);
    }

    // clean up pruned street vertices
    // note that visibility vertices must not be removed from the graph
    // because serialization will break. Edge lists are reconstructed
    // only for graph vertices after loading the graph
    List<AreaEdge> areaEdges = graph.getEdgesOfType(AreaEdge.class);
    HashSet<AreaGroup> areas = new HashSet<>();
    HashSet<Vertex> visibilityVertices = new HashSet<>();

    for (AreaEdge ae : areaEdges) {
      areas.add(ae.getArea());
    }
    for (AreaGroup a : areas) {
      visibilityVertices.addAll(a.visibilityVertices());
    }

    int removed = 0;
    List<Vertex> toRemove = new LinkedList<>();
    for (Vertex v : graph.getVerticesOfType(StreetVertex.class)) {
      if (v.getDegreeOut() + v.getDegreeIn() == 0 && !visibilityVertices.contains(v)) {
        toRemove.add(v);
      }
    }
    for (Vertex v : toRemove) {
      graph.remove(v);
      removed += 1;
    }
    LOG.info("Removed {} edgeless street vertices", removed);

    LOG.info(
      "Island pruning completed in {}",
      DurationUtils.durationToStr(Duration.between(start, Instant.now()))
    );
  }

  /**
   * Applies the pruning decisions computed by {@link IslandFinder#computeIslands}. This is where
   * the graph is actually mutated (edges removed, permissions restricted), so unlike
   * {@code computeIslands} it must not be run concurrently for different traverse modes.
   */
  private void applyIslandPruning(IslandComputation computation) {
    int count = applyPruning(
      computation.islands(),
      computation.isolated(),
      computation.traverseMode()
    );
    LOG.info("Modified {} {} islands", count, computation.traverseMode());
  }

  /**
   * Applies the pruning decision for {@code traverseMode}: converts edges reachable only via
   * noThruTraffic to noThruTraffic, restricts or removes edges that are genuinely isolated
   * (as recorded by {@link IslandFinder#computeIslands} in {@code isolated}), and unlinks
   * stranded stops. This mutates the graph.
   */
  private int applyPruning(
    ArrayList<Subgraph> islands,
    Set<Edge> isolated,
    TraverseMode traverseMode
  ) {
    var stats = new PruningStats();
    for (Subgraph island : islandFinder.islandsToPrune(islands, stats)) {
      restrictOrRemoveIslandEdges(island, isolated, stats, traverseMode);
      if (island.stopSize() > 0) {
        stats.incrementIslandsWithStopsChanged();
      }
      stats.incrementModifiedIslands();
    }
    LOG.info("Number of islands with stops: {}", stats.islandsWithStops());
    LOG.info("Modified connectivity of {} islands with stops", stats.islandsWithStopsChanged());
    LOG.info("Removed {} edges", stats.removed());
    LOG.info("Removed traversal mode from {} edges", stats.restricted());
    LOG.info("Converted {} edges to noThruTraffic", stats.noThru());
    issueStore.add(
      new GraphConnectivity(
        traverseMode,
        islands.size(),
        stats.islandsWithStops(),
        stats.islandsWithStopsChanged(),
        stats.removed(),
        stats.restricted(),
        stats.noThru()
      )
    );
    return stats.modifiedIslands();
  }

  /**
   * Converts edges of {@code island} that are reachable only via noThruTraffic to noThruTraffic,
   * restricts or removes edges that are genuinely isolated (per {@code isolated}), and unlinks
   * any stops stranded by pruning. Mutates the graph.
   */
  private void restrictOrRemoveIslandEdges(
    Subgraph island,
    Set<Edge> isolated,
    PruningStats stats,
    TraverseMode traverseMode
  ) {
    int nothru = 0;
    int removed = 0;
    int restricted = 0;
    //iterate over the street vertex of the subgraph
    for (Vertex v : island.streetVertices()) {
      Collection<Edge> outgoing = new ArrayList<>(v.getOutgoing());
      for (Edge e : outgoing) {
        if (e instanceof StreetEdge) {
          StreetEdge pse = (StreetEdge) e;
          if (!isolated.contains(e)) {
            boolean changed = false;

            // not a true island edge but has limited access
            // so convert to noThruTraffic
            if (traverseMode == TraverseMode.CAR) {
              if (!pse.isMotorVehicleNoThruTraffic()) {
                pse.setMotorVehicleNoThruTraffic(true);
                changed = true;
              }
            } else if (traverseMode == TraverseMode.BICYCLE) {
              if (!pse.isBicycleNoThruTraffic()) {
                pse.setBicycleNoThruTraffic(true);
                changed = true;
              }
            } else if (traverseMode == TraverseMode.WALK) {
              if (!pse.isWalkNoThruTraffic()) {
                pse.setWalkNoThruTraffic(true);
                changed = true;
              }
            }
            if (changed) {
              stats.incrementNoThru();
              nothru++;
            }
          } else {
            StreetTraversalPermission permission = pse.getPermission();
            boolean changed = false;
            if (traverseMode == TraverseMode.CAR) {
              if (permission.allows(StreetTraversalPermission.CAR)) {
                permission = permission.remove(StreetTraversalPermission.CAR);
                changed = true;
              }
            } else if (traverseMode == TraverseMode.BICYCLE) {
              if (permission.allows(StreetTraversalPermission.BICYCLE)) {
                permission = permission.remove(StreetTraversalPermission.BICYCLE);
                changed = true;
              }
            } else if (traverseMode == TraverseMode.WALK) {
              if (permission.allows(StreetTraversalPermission.PEDESTRIAN)) {
                permission = permission.remove(StreetTraversalPermission.PEDESTRIAN);
                changed = true;
              }
            }
            if (changed) {
              if (permission == StreetTraversalPermission.NONE) {
                graph.removeEdge(pse);
                stats.incrementRemoved();
                removed++;
              } else {
                pse.setPermission(permission);
                stats.incrementRestricted();
                restricted++;
              }
            }
          }
        }
      }
    }

    if (traverseMode == TraverseMode.WALK) {
      // note: do not unlink stop if only CAR mode is pruned
      // maybe this needs more logic for flex routing cases
      List<VertexLabel> stopLabels = new ArrayList<>();
      for (var v : island.stopVertices()) {
        stopLabels.add(v.getLabel());
        Collection<Edge> edges = new ArrayList<>(v.getOutgoing());
        edges.addAll(v.getIncoming());
        for (Edge e : edges) {
          graph.removeEdge(e);
        }
      }
      if (island.stopSize() > 0) {
        // issue about stops that got unlinked in pruning
        issueStore.add(
          new PrunedStopIsland(
            island,
            nothru,
            restricted,
            removed,
            stopLabels.stream().map(Object::toString).collect(Collectors.joining(","))
          )
        );
      }
    }
    issueStore.add(new GraphIsland(island, nothru, restricted, removed, traverseMode.name()));
  }
}
