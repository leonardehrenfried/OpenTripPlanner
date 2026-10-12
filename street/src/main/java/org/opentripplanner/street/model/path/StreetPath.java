package org.opentripplanner.street.model.path;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.locationtech.jts.geom.LineString;
import org.opentripplanner.street.model.edge.Edge;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.search.request.StreetSearchRequest;
import org.opentripplanner.street.search.state.State;

/// A path within the street network, the result of a street search.
///
/// The states of the search are deliberately not exposed. To get the details of the path, split
/// it into [StreetLeg]s using [#legs(double)].
public interface StreetPath {
  /// Build a chronologically ordered path by following the back-state chain of `finalState` all
  /// the way back to the origin of the search. Paths from arriveBy searches are reversed, so the
  /// path always runs forward in time.
  static StreetPath of(State finalState) {
    return new DefaultStreetPath(finalState);
  }

  /// The generalized cost (A* weight) of traversing the path.
  double generalizedCost();

  /// The time it takes to traverse the path, rounded up to whole seconds.
  Duration duration();

  /// The total elevation gained and lost along the path.
  ElevationChange elevationChange();

  /// Whether the path ends while still renting a vehicle picked up at a rental station.
  boolean arrivesWithRentedVehicleFromStation();

  /// Split this path into legs. Each change of street mode, like picking up a rental vehicle or
  /// parking a car, starts a new leg. Walking a bike does not.
  ///
  /// The expensive parts of each leg, like the turn-by-turn directions, are computed lazily when
  /// accessed.
  ///
  /// @param ellipsoidToGeoidDifference The difference between the ellipsoid and the geoid
  ///                                   elevation of the graph, applied to the elevations of the
  ///                                   legs if requested.
  List<StreetLeg> legs(double ellipsoidToGeoidDifference);

  /// The distance traversed along the path in meters.
  double traversalDistance();

  /// Traverse the edges of this path again, starting at the same vertex, using the given request.
  /// This is used for paths computed ahead of time, for example during graph build, to get the
  /// times and costs for the preferences of a specific search.
  ///
  /// @return Empty if the edges can't be traversed using the given request
  Optional<StreetPath> reTraverse(StreetSearchRequest request);

  /// Continue the street search which produced this path by traversing the given edge. For paths
  /// from arriveBy searches the edge is traversed backward in time, so it is added in front of
  /// the path.
  ///
  /// @return Empty if the edge can't be traversed
  Optional<StreetPath> continueSearch(Edge edge);

  /// Continue the street search which produced this path along the edges of the given path. For
  /// paths from arriveBy searches the edges are traversed backward in time, so the given path is
  /// added in front of this path.
  ///
  /// @return Empty if the edges can't be traversed
  Optional<StreetPath> continueSearch(StreetPath path);

  /// The vertex where the path starts, in chronological order.
  Vertex fromVertex();

  /// The vertex where the path ends, in chronological order.
  Vertex toVertex();

  /// True if the path has no edges, so it starts and ends at the same vertex.
  boolean isEmpty();

  /// The geometry of the path.
  LineString geometry();

  /// True if all edges are traversed on foot.
  boolean containsOnlyWalkMode();

  /// True if any part of the path is traversed by car.
  boolean containsModeCar();
}
