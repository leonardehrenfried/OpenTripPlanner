package org.opentripplanner.graph_builder.module.islandpruning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.opentripplanner.street.graph.Graph;
import org.opentripplanner.street.model.StreetMode;
import org.opentripplanner.street.model.edge.Edge;
import org.opentripplanner.street.model.edge.StreetEdge;
import org.opentripplanner.street.model.vertex.StreetVertex;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.search.TraverseMode;
import org.opentripplanner.street.search.request.StreetSearchRequest;
import org.opentripplanner.street.search.state.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Identifies the islands present in the street graph for a given traverse mode, and which edges
 * become unreachable if those islands are pruned. Purely read-only: nothing here ever mutates the
 * graph, which is what allows {@link IslandPruningModule} to run {@link #computeIslands}
 * concurrently for every traverse mode.
 */
class IslandFinder {

  private static final Logger LOG = LoggerFactory.getLogger(IslandFinder.class);

  private final Graph graph;
  private final IslandPruningParameters parameters;

  IslandFinder(Graph graph, IslandPruningParameters parameters) {
    this.graph = graph;
    this.parameters = parameters;
  }

  /**
   * Island pruning strategy:
   * 1. Extract islands without using noThruTraffic edges at all.
   * 2. Then create expanded islands by accepting noThruTraffic edges, but do not jump across
   *    original islands! Note: these expanded islands can overlap.
   * 3. Relax connectivity even more: generate islands by allowing jumps between islands. Find out
   *    unreachable edges of small islands.
   * 4. Analyze small expanded islands (from step 2). Convert edges which are reachable only via
   *    noThruTraffic edges to noThruTraffic state. Remove traversal mode specific access from
   *    unreachable edges. Remove unconnected edges.
   */
  IslandComputation computeIslands(TraverseMode traverseMode) {
    Map<Vertex, Subgraph> subgraphs = new HashMap<>();
    Map<Vertex, Subgraph> extgraphs = new HashMap<>();
    Map<Vertex, ArrayList<Vertex>> neighborsForVertex = new HashMap<>();
    ArrayList<Subgraph> islands = new ArrayList<>();
    int count;

    /* establish vertex neighbourhood without currently relevant noThruTrafficEdges */
    collectNeighbourVertices(neighborsForVertex, traverseMode, false);

    /* associate each connected vertex with a subgraph */
    count = collectSubGraphs(neighborsForVertex, subgraphs, null, null);
    LOG.info("Islands when {} noThruTraffic is considered: {}", traverseMode, count);

    /* Expand vertex neighbourhood with relevant noThruTrafficEdges
       Note that we can reuse the original neighbour map here
       and simply process a smaller set of noThruTrafficEdges */
    collectNeighbourVertices(neighborsForVertex, traverseMode, true);

    /* Next: generate subgraphs without considering access limitations */
    count = collectSubGraphs(neighborsForVertex, extgraphs, null, islands);
    LOG.info("Islands when {} noThruTraffic is ignored: {}", traverseMode, count);

    /* collect unreachable edges to a set */
    Set<Edge> isolated = markIsolatedEdges(islands);

    extgraphs = new HashMap<>();
    islands = new ArrayList<>();

    /* Recompute expanded subgraphs by accepting noThruTraffic edges in graph expansion.
       However, expansion is not allowed to jump from an original island to another one
     */
    collectSubGraphs(neighborsForVertex, extgraphs, subgraphs, islands);

    /* Next round: generate purely noThruTraffic islands if such ones exist */
    count = collectSubGraphs(neighborsForVertex, extgraphs, null, islands);

    LOG.info("{} noThruTraffic island count: {}", traverseMode, count);

    LOG.info("Total {} sub graphs found", islands.size());

    return new IslandComputation(traverseMode, islands, isolated);
  }

  /**
   * Records every edge that is reachable only through islands small enough to be pruning
   * candidates. Read-only: nothing about the graph is mutated, only the returned set.
   */
  private Set<Edge> markIsolatedEdges(List<Subgraph> islands) {
    var stats = new PruningStats();
    Set<Edge> isolated = new HashSet<>();
    for (Subgraph island : islandsToPrune(islands, stats)) {
      for (Vertex v : island.streetVertices()) {
        for (Edge e : v.getOutgoing()) {
          if (e instanceof StreetEdge) {
            isolated.add(e);
            stats.incrementIsolated();
          }
        }
      }
    }
    LOG.info("Detected {} isolated edges", stats.isolated());
    return isolated;
  }

  /**
   * The islands below the configured size threshold for the current pruning pass, excluding the
   * largest island (which is never pruned). Shared with {@link IslandPruningModule}'s pruning
   * step, so both agree on exactly which islands qualify.
   */
  List<Subgraph> islandsToPrune(List<Subgraph> islands, PruningStats stats) {
    Subgraph largest = findLargestIsland(islands);
    double adaptivePruningFactor = parameters.adaptivePruningFactor();
    int adaptivePruningDistance = parameters.adaptivePruningDistance();
    List<Subgraph> toPrune = new ArrayList<>();

    for (Subgraph island : islands) {
      if (island == largest) {
        continue;
      }
      if (island.stopSize() > 0) {
        //for islands with stops
        stats.incrementIslandsWithStops();
        boolean onlyFerry = island.hasOnlyFerryStops();
        int pruningThresholdWithStops = parameters.pruningThresholdIslandWithStops();
        // do not remove real islands which have only ferry stops
        if (!onlyFerry && island.streetSize() < pruningThresholdWithStops * adaptivePruningFactor) {
          double sizeCoeff = (adaptivePruningFactor > 1.0)
            ? island.distanceFromOtherGraph(graph, adaptivePruningDistance) /
              adaptivePruningDistance
            : 1.0;

          if (island.streetSize() * sizeCoeff < pruningThresholdWithStops) {
            toPrune.add(island);
          }
        }
      } else {
        //for islands without stops
        int pruningThresholdWithoutStops = parameters.pruningThresholdIslandWithoutStops();
        if (island.streetSize() < pruningThresholdWithoutStops * adaptivePruningFactor) {
          double sizeCoeff = (adaptivePruningFactor > 1.0)
            ? island.distanceFromOtherGraph(graph, adaptivePruningDistance) /
              adaptivePruningDistance
            : 1.0;
          if (island.streetSize() * sizeCoeff < pruningThresholdWithoutStops) {
            toPrune.add(island);
          }
        }
      }
    }
    return toPrune;
  }

  private Subgraph findLargestIsland(List<Subgraph> islands) {
    Subgraph largest = null;
    int maxSize = 0;
    for (Subgraph island : islands) {
      int streetCount = island.streetSize();
      if (streetCount >= maxSize) {
        maxSize = streetCount;
        largest = island;
      }
    }
    return largest;
  }

  private void collectNeighbourVertices(
    Map<Vertex, ArrayList<Vertex>> neighborsForVertex,
    TraverseMode traverseMode,
    boolean shouldMatchNoThruType
  ) {
    StreetMode streetMode = switch (traverseMode) {
      case WALK -> StreetMode.WALK;
      case BICYCLE -> StreetMode.BIKE;
      case CAR -> StreetMode.CAR;
      default -> throw new IllegalArgumentException();
    };

    StreetSearchRequest request = StreetSearchRequest.of().withMode(streetMode).build();

    for (Vertex gv : graph.getVertices()) {
      if (!(gv instanceof StreetVertex)) {
        continue;
      }
      State s0 = new State(gv, request);
      for (Edge e : gv.getOutgoing()) {
        if (
          e instanceof StreetEdge &&
          shouldMatchNoThruType != ((StreetEdge) e).isNoThruTraffic(traverseMode)
        ) {
          continue;
        }
        State[] states = e.traverse(s0);
        if (State.isEmpty(states)) {
          continue;
        }
        Arrays.stream(states)
          .map(State::getVertex)
          .forEach(out -> {
            var vertexList = neighborsForVertex.computeIfAbsent(gv, k -> new ArrayList<>());
            vertexList.add(out);

            // note: this assumes that edges are bi-directional. Maybe explicit state traversal is needed for CAR mode.
            vertexList = neighborsForVertex.computeIfAbsent(out, k -> new ArrayList<>());
            vertexList.add(gv);
          });
      }
    }
  }

  private int collectSubGraphs(
    Map<Vertex, ArrayList<Vertex>> neighborsForVertex,
    // put new subgraphs here
    Map<Vertex, Subgraph> newgraphs,
    // optional isolation map from a previous round
    Map<Vertex, Subgraph> subgraphs,
    // final list of islands or null
    ArrayList<Subgraph> islands
  ) {
    int count = 0;
    for (Vertex gv : graph.getVertices()) {
      if (!(gv instanceof StreetVertex)) {
        continue;
      }

      if (subgraphs != null && !subgraphs.containsKey(gv)) {
        // do not start new graph generation from non-classified vertex
        continue;
      }
      // already processed
      if (newgraphs.containsKey(gv)) {
        continue;
      }
      if (!neighborsForVertex.containsKey(gv)) {
        continue;
      }
      Subgraph subgraph = computeConnectedSubgraph(neighborsForVertex, gv, subgraphs, newgraphs);
      for (var subnode : subgraph.streetVertices()) {
        newgraphs.put(subnode, subgraph);
      }
      if (islands != null) {
        islands.add(subgraph);
      }
      count++;
    }
    return count;
  }

  private Subgraph computeConnectedSubgraph(
    Map<Vertex, ArrayList<Vertex>> neighborsForVertex,
    Vertex startVertex,
    Map<Vertex, Subgraph> anchors,
    Map<Vertex, Subgraph> alreadyMapped
  ) {
    Subgraph subgraph = new Subgraph();
    Queue<Vertex> q = new LinkedList<>();
    Subgraph anchor = null;

    if (anchors != null) {
      // anchor subgraph expansion to this subgraph
      anchor = anchors.get(startVertex);
    }
    q.add(startVertex);
    while (!q.isEmpty()) {
      Vertex vertex = q.poll();
      for (Vertex neighbor : neighborsForVertex.get(vertex)) {
        if (!subgraph.contains(neighbor) && !alreadyMapped.containsKey(neighbor)) {
          if (anchor != null) {
            Subgraph compare = anchors.get(neighbor);
            if (compare != null && compare != anchor) {
              // do not enter a new island
              continue;
            }
          }
          subgraph.addVertex(neighbor);
          q.add(neighbor);
        }
      }
    }
    return subgraph;
  }
}
