package org.opentripplanner.street.model.path;

import java.util.List;
import java.util.Optional;
import org.locationtech.jts.geom.LineString;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.search.request.StreetSearchRequest;

/// A chronological path within the street network.
///
/// To get the details of the path, split it into [StreetLeg]s using [#computeLegs(double)].
///
/// A path is also a [StreetSearchResult] whose [#toPath()] is free, so code which handles both
/// search results and ready-made paths, like access and egress, can treat them alike.
public interface StreetPath extends StreetSearchResult {
  /// A path which is not the result of a street search, used when there is no street path, like
  /// for a stop found by a straight-line search. It has no edges, no vertices and no cost.
  StreetPath EMPTY = EmptyStreetPath.INSTANCE;

  /// Combine paths or search results which follow each other in time into a single path, for example a walk, a ride
  /// on a flexible trip and another walk. Each path must start where and when the previous one
  /// ends.
  static StreetPath concat(StreetSearchResult... parts) {
    return new CompositeStreetPath(List.of(parts));
  }

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

  /// Traverse the edges of this path again, starting at the same vertex, using the given request.
  /// This is used for paths computed ahead of time, for example during graph build, to get the
  /// times and costs for the preferences of a specific search.
  ///
  /// @return Empty if the edges can't be traversed using the given request
  Optional<StreetPath> reTraverse(StreetSearchRequest request);

  /// The vertex where the path starts, in chronological order.
  Vertex fromVertex();

  /// The vertex where the path ends, in chronological order.
  Vertex toVertex();

  /// True if the path has no edges, so it starts and ends at the same vertex.
  boolean isEmpty();

  /// The geometry of the path.
  LineString geometry();

  /// A path is already chronological, so this returns the path itself.
  @Override
  default StreetPath toPath() {
    return this;
  }
}
