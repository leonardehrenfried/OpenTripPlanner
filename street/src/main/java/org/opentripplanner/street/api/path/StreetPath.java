package org.opentripplanner.street.api.path;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.locationtech.jts.geom.LineString;
import org.opentripplanner.street.api.request.StreetSearchRequest;
import org.opentripplanner.street.model.vertex.Vertex;

/// A path within the street network, the result of a street search.
///
/// The states of the search are deliberately not exposed. To get the details of the path, split
/// it into [StreetLeg]s using [#computeLegs(double)].
public interface StreetPath {
  /// A path which is not the result of a street search, used when there is no street path, like
  /// for a stop found by a straight-line search. It has no edges, no vertices and no cost.
  StreetPath EMPTY = EmptyStreetPath.INSTANCE;

  /// Combine paths which follow each other in time into a single path, for example a walk, a ride
  /// on a flexible trip and another walk. Each path must start where and when the previous one
  /// ends.
  static StreetPath concat(StreetPath... paths) {
    return new CompositeStreetPath(List.of(paths));
  }

  /// The generalized cost (A* weight) of traversing the path.
  double generalizedCost();

  /// The time it takes to traverse the path, rounded up to whole seconds.
  Duration duration();

  /// The time the path starts, with millisecond precision.
  Instant startTime();

  /// The time the path ends, with millisecond precision.
  Instant endTime();

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
  List<StreetLeg> computeLegs(double ellipsoidToGeoidDifference);

  /// The distance traversed along the path in meters.
  double traversalDistance();

  /// Traverse the edges of this path again, starting at the same vertex, using the given request.
  /// This is used for paths computed ahead of time, for example during graph build, to get the
  /// times and costs for the preferences of a specific search.
  ///
  /// @return Empty if the edges can't be traversed using the given request
  Optional<StreetPath> reTraverse(StreetSearchRequest request);

  /// Continue the street search which produced this path along the edges of the given path. For
  /// paths from arriveBy searches the edges are traversed backward in time, so the given path is
  /// added in front of this path.
  ///
  /// @return Empty if the edges can't be traversed
  Optional<StreetPath> continueSearch(StreetPath path);

  /// A copy of this path without the states of the search, keeping only what can be computed from
  /// the edges. Use this for paths which are kept for a long time, like transfers, so that the
  /// search states can be garbage collected. Use [#reTraverse(StreetSearchRequest)] to get a
  /// complete path again.
  StreetPath withoutStates();

  /// Continue the street search which produced this path along the edges of the given path, after
  /// a gap which is not part of the street network, like a ride on a flexible trip. The search is
  /// restarted with the same request at the start of `path`, `gap` after this path ends. For paths
  /// from arriveBy searches it is restarted at the end of `path`, `gap` before this path starts.
  /// Since a search starts at a whole second, the time this path ends (starts) is rounded up (down)
  /// to whole seconds first.
  ///
  /// Unlike [#continueSearch(StreetPath)], the returned path contains only the edges of `path`.
  /// Use [#concat(StreetPath...)] to combine it with this path and the gap.
  ///
  /// @return Empty if the edges can't be traversed
  Optional<StreetPath> continueSearchAfter(Duration gap, StreetPath path);

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
