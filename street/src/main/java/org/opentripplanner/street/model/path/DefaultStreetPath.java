package org.opentripplanner.street.model.path;

import static org.opentripplanner.street.model.path.ElevationProfileEncoder.encodeElevationProfileWithNaN;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.impl.PackedCoordinateSequence;
import org.opentripplanner.street.geometry.GeometryUtils;
import org.opentripplanner.street.model.edge.Edge;
import org.opentripplanner.street.model.edge.StreetEdge;
import org.opentripplanner.street.model.elevation.ElevationProfile;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.search.request.StreetSearchRequest;
import org.opentripplanner.street.search.state.EdgeTraverser;
import org.opentripplanner.street.search.state.State;

/// A [StreetPath] backed by the states of a street search.
///
/// The edges, the endpoints and the cost, duration and distance are computed when the path is
/// created. The chronologically ordered states are only built when they are needed, for example to
/// create the legs. This matters for arriveBy searches, where building the states means reversing
/// the whole state chain.
///
/// The path is serializable, so it can be stored in the graph, for example as part of a transfer.
/// The states are not serialized, so a deserialized path only offers what can be computed from the
/// edges. Use [#reTraverse(StreetSearchRequest)] to get a complete path for a specific request.
public final class DefaultStreetPath implements StreetPath, Serializable {

  private static final String NO_STATES_MESSAGE =
    "The states of a deserialized path are not available. Use reTraverse() to create them.";

  /// The final state of the search in search order. For arriveBy searches the back-state chain of
  /// this state runs backward in time. Null if the path was deserialized.
  @Nullable
  private final transient State searchState;

  /// The states in chronological order, built lazily for paths created from a search.
  @Nullable
  private transient List<State> states;

  /// The edges in chronological order.
  private final List<Edge> edges;

  private final Vertex fromVertex;
  private final Vertex toVertex;
  private final boolean arriveBy;
  private final double generalizedCost;
  private final Duration duration;
  private final double traversalDistance;

  /// Create a path from chronologically ordered states and edges, which run forward in time.
  public DefaultStreetPath(List<State> states, List<Edge> edges) {
    validate(states, edges);
    var first = states.getFirst();
    var last = states.getLast();
    this.searchState = last;
    this.states = states;
    this.edges = edges;
    this.fromVertex = first.getVertex();
    this.toVertex = last.getVertex();
    this.arriveBy = false;
    this.generalizedCost = last.getWeight() - first.getWeight();
    long millis = Math.abs(last.getTimeMilliseconds() - first.getTimeMilliseconds());
    this.duration = Duration.ofSeconds((millis + 999L) / 1000L);
    this.traversalDistance = last.getTraversalDistanceMeters() - first.getTraversalDistanceMeters();
  }

  /// Create a path from the final state of a street search, following the back-state chain all
  /// the way back to the origin of the search. Paths from arriveBy searches are reversed, so the
  /// path always runs forward in time.
  public DefaultStreetPath(State finalState) {
    this.searchState = Objects.requireNonNull(finalState);
    this.arriveBy = finalState.getRequest().arriveBy();

    // Collect the edges and find the origin of the search without building the states
    var list = new ArrayList<Edge>();
    var origin = finalState;
    while (origin.getBackState() != null) {
      list.add(origin.getBackEdge());
      origin = origin.getBackState();
    }
    // The back-state chain of a depart-after search runs backward in time
    if (!arriveBy) {
      Collections.reverse(list);
    }
    this.edges = list;
    this.fromVertex = arriveBy ? finalState.getVertex() : origin.getVertex();
    this.toVertex = arriveBy ? origin.getVertex() : finalState.getVertex();
    this.generalizedCost = finalState.getWeight();
    this.duration = Duration.ofSeconds(finalState.getElapsedTimeSeconds());
    this.traversalDistance = finalState.getTraversalDistanceMeters();
  }

  private static void validate(List<State> states, List<Edge> edges) {
    if (states.isEmpty()) {
      throw new IllegalArgumentException("A path needs at least one state");
    }
    if (edges.size() != states.size() - 1) {
      throw new IllegalArgumentException("A path needs an edge between each state");
    }
  }

  @Override
  public double generalizedCost() {
    return generalizedCost;
  }

  /// The duration is computed from the millisecond precision times of the first and last state
  /// and rounded up, the same way as [State#getElapsedTimeSeconds()].
  @Override
  public Duration duration() {
    return duration;
  }

  @Override
  public double traversalDistance() {
    return traversalDistance;
  }

  @Override
  public ElevationChange elevationChange() {
    return calculateElevations();
  }

  @Override
  public boolean arrivesWithRentedVehicleFromStation() {
    return lastState().isRentingVehicleFromStation();
  }

  @Override
  public List<StreetLeg> legs(double ellipsoidToGeoidDifference) {
    return new StreetPathToLegsMapper(ellipsoidToGeoidDifference).map(this);
  }

  @Override
  public Optional<StreetPath> reTraverse(StreetSearchRequest request) {
    return EdgeTraverser.createPath(fromVertex, edges, request);
  }

  @Override
  public Optional<StreetPath> continueSearch(Edge edge) {
    return continueSearchAlong(List.of(edge));
  }

  @Override
  public Optional<StreetPath> continueSearch(StreetPath path) {
    var pathEdges = ((DefaultStreetPath) path).edges();
    return continueSearchAlong(arriveBy ? pathEdges.reversed() : pathEdges);
  }

  @Override
  public Vertex fromVertex() {
    return fromVertex;
  }

  @Override
  public Vertex toVertex() {
    return toVertex;
  }

  @Override
  public boolean isEmpty() {
    return edges.isEmpty();
  }

  @Override
  public LineString geometry() {
    var geometries = edges
      .stream()
      .filter(Edge::includeGeometryInPath)
      .map(Edge::getGeometry)
      .filter(Objects::nonNull);

    return GeometryUtils.concatenateLineStrings(geometries::iterator);
  }

  @Override
  public boolean containsOnlyWalkMode() {
    return requireSearchState().containsOnlyWalkMode();
  }

  @Override
  public boolean containsModeCar() {
    return requireSearchState().containsModeCar();
  }

  /// Two paths are equal if they traverse the same edges.
  @Override
  public boolean equals(Object o) {
    return o instanceof DefaultStreetPath other && edges.equals(other.edges);
  }

  @Override
  public int hashCode() {
    return edges.hashCode();
  }

  @Override
  public String toString() {
    return "DefaultStreetPath{" + fromVertex + " -> " + toVertex + ", " + duration + "}";
  }

  /// The start of the path in seconds
  Instant startTime() {
    return states().getFirst().getTime();
  }

  /// The end of the path in seconds
  Instant endTime() {
    return states().getLast().getTime();
  }

  double weight() {
    var states = states();
    return states.getLast().weight - states.getFirst().weight;
  }

  double distanceMeters() {
    return edges.stream().mapToDouble(Edge::getDistanceMeters).sum();
  }

  /// Get all the states of this path in chronological order
  List<State> states() {
    if (states == null) {
      var state = arriveBy ? requireSearchState().reverse() : requireSearchState();
      var list = new ArrayList<State>();
      for (State s : state.listBackStates()) {
        list.add(s);
      }
      Collections.reverse(list);
      states = list;
    }
    return states;
  }

  /// Get all the edges of this path in chronological order
  List<Edge> edges() {
    return edges;
  }

  /// Get the last state in the path
  State lastState() {
    return states().getLast();
  }

  ElevationProfile elevation(boolean geoidElevation, double ellipsoidToGeoidDifference) {
    var builder = ElevationProfile.of();

    double heightOffset = geoidElevation ? ellipsoidToGeoidDifference : 0;

    double distanceOffset = 0;
    for (final Edge edge : edges) {
      if (edge.getDistanceMeters() > 0) {
        builder.add(encodeElevationProfileWithNaN(edge, distanceOffset, heightOffset));
        distanceOffset += edge.getDistanceMeters();
      }
    }

    var p = builder.build();

    return p.isAllYUnknown() ? null : p;
  }

  /// Calculate the elevationGained and elevationLost
  ElevationChange calculateElevations() {
    double elevationGained_m = 0.0;
    double elevationLost_m = 0.0;
    for (Edge edge : edges) {
      if (!(edge instanceof StreetEdge edgeWithElevation)) {
        continue;
      }
      PackedCoordinateSequence coordinates = edgeWithElevation.getElevationProfile();

      if (coordinates == null) {
        continue;
      }
      // TODO Check the test below, AFAIU current elevation profile has 3 dimensions.
      if (coordinates.getDimension() != 2) {
        continue;
      }

      for (int i = 0; i < coordinates.size() - 1; i++) {
        double change_m = coordinates.getOrdinate(i + 1, 1) - coordinates.getOrdinate(i, 1);
        if (change_m > 0.0) {
          elevationGained_m += change_m;
        } else {
          elevationLost_m -= change_m;
        }
      }
    }
    return new ElevationChange(elevationGained_m, elevationLost_m);
  }

  /// Get a specific section of this path as a new path.
  ///
  /// @param startIdx the first state index (inclusive)
  /// @param endIdx the end state index (exclusive)
  DefaultStreetPath subPath(int startIdx, int endIdx) {
    var subStates = states().subList(startIdx, endIdx);
    var subEdges = edges.subList(startIdx, endIdx - 1);
    return new DefaultStreetPath(subStates, subEdges);
  }

  private State requireSearchState() {
    if (searchState == null) {
      throw new IllegalStateException(NO_STATES_MESSAGE);
    }
    return searchState;
  }

  private Optional<StreetPath> continueSearchAlong(List<Edge> edgesInSearchOrder) {
    return EdgeTraverser.traverseEdges(requireSearchState(), edgesInSearchOrder).map(
      DefaultStreetPath::new
    );
  }
}
