package org.opentripplanner.street.model.path;

import static org.opentripplanner.street.model.path.ElevationProfileEncoder.encodeElevationProfileWithNaN;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
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

/// A [StreetPath] backed by the states of a street search, in chronological order.
///
/// The path is always chronological: the states of an arriveBy search are reversed before the path
/// is created, see [DefaultStreetSearchResult#toPath()].
///
/// A path may have no states, see [#withoutStates()], for example when it is stored in the graph as
/// part of a transfer. The states are also not serialized. A path without states only offers what
/// can be computed from the edges. Use [#reTraverse(StreetSearchRequest)] to get a complete path
/// for a specific request.
public final class DefaultStreetPath implements StreetPath, Serializable {

  private static final String NO_STATES_MESSAGE =
    "The path has no states, it was stored or deserialized. Use reTraverse() to create them.";

  /// The start and end time of the path with millisecond precision. Null if the path was
  /// deserialized.
  @Nullable
  private final transient Instant startTime;

  @Nullable
  private final transient Instant endTime;

  /// The states in chronological order, or null if the path has no states.
  @Nullable
  private final transient List<State> states;

  /// The edges in chronological order.
  private final List<Edge> edges;

  private final Vertex fromVertex;
  private final Vertex toVertex;
  private final double generalizedCost;
  private final Duration duration;
  private final double traversalDistance;

  /// Create a path from chronologically ordered states and edges, which run forward in time.
  public DefaultStreetPath(List<State> states, List<Edge> edges) {
    validate(states, edges);
    var first = states.getFirst();
    var last = states.getLast();
    this.states = states;
    this.edges = edges;
    this.fromVertex = first.getVertex();
    this.toVertex = last.getVertex();
    this.startTime = timeOf(first);
    this.endTime = timeOf(last);
    this.generalizedCost = last.getWeight() - first.getWeight();
    long millis = Math.abs(last.getTimeMilliseconds() - first.getTimeMilliseconds());
    this.duration = Duration.ofSeconds((millis + 999L) / 1000L);
    this.traversalDistance = last.getTraversalDistanceMeters() - first.getTraversalDistanceMeters();
  }

  /// Copy the given path without its states.
  private DefaultStreetPath(DefaultStreetPath other) {
    this(
      other.fromVertex,
      other.toVertex,
      other.edges,
      other.startTime,
      other.endTime,
      other.generalizedCost,
      other.duration,
      other.traversalDistance
    );
  }

  /// Create a chronological path without states, from what is known about a street search.
  DefaultStreetPath(
    Vertex fromVertex,
    Vertex toVertex,
    List<Edge> edges,
    Instant startTime,
    Instant endTime,
    double generalizedCost,
    Duration duration,
    double traversalDistance
  ) {
    this.states = null;
    this.fromVertex = fromVertex;
    this.toVertex = toVertex;
    this.edges = edges;
    this.startTime = startTime;
    this.endTime = endTime;
    this.generalizedCost = generalizedCost;
    this.duration = duration;
    this.traversalDistance = traversalDistance;
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
  public Instant startTime() {
    return requireTime(startTime);
  }

  @Override
  public Instant endTime() {
    return requireTime(endTime);
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
  public List<StreetLeg> computeLegs(double ellipsoidToGeoidDifference) {
    return new StreetPathToLegsMapper(ellipsoidToGeoidDifference).map(this);
  }

  @Override
  public Optional<StreetPath> reTraverse(StreetSearchRequest request) {
    return EdgeTraverser.createPath(fromVertex, edges, request);
  }

  @Override
  public StreetPath withoutStates() {
    return states == null ? this : new DefaultStreetPath(this);
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
    return lastState().containsOnlyWalkMode();
  }

  @Override
  public boolean containsModeCar() {
    return lastState().containsModeCar();
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
      throw new IllegalStateException(NO_STATES_MESSAGE);
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

  private static Instant timeOf(State state) {
    return Instant.ofEpochMilli(state.getTimeMilliseconds());
  }

  private static Instant requireTime(@Nullable Instant time) {
    if (time == null) {
      throw new IllegalStateException(NO_STATES_MESSAGE);
    }
    return time;
  }
}
