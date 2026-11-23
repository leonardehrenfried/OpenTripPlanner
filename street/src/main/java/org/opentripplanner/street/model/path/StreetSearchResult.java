package org.opentripplanner.street.model.path;

import java.time.Duration;
import java.time.Instant;

/// What is known about a way through the street network without building its chronological
/// [StreetPath].
///
/// Everything except [#toPath()] is cheap. Use this type for the many candidates a search
/// produces, like the access and egress candidates of a transit search, and call [#toPath()]
/// only for the ones which are actually used, like when mapping the legs of an itinerary.
///
/// There are two kinds of results:
///
/// - [DefaultStreetSearchResult] wraps the final state of a street search. Building its path
///   reverses the state chain for arriveBy searches, which is expensive.
/// - A [StreetPath] is a result which is already chronological, like a path combining a walk and
///   a ride on a flexible trip. Its [#toPath()] returns the path itself.
public interface StreetSearchResult {
  /// The generalized cost (A* weight) of traversing the path.
  double generalizedCost();

  /// The time it takes to traverse the path, rounded up to whole seconds.
  Duration duration();

  /// The time the path starts, with millisecond precision.
  Instant startTime();

  /// The time the path ends, with millisecond precision.
  Instant endTime();

  /// The distance traversed along the path in meters.
  double traversalDistance();

  /// True if any part of the path is traversed by car.
  boolean containsModeCar();

  /// True if all edges are traversed on foot.
  boolean containsOnlyWalkMode();

  /// The chronological path. This may be expensive, see the class documentation.
  StreetPath toPath();

  /// The chronological path without the states of the search, keeping only what can be computed
  /// from the edges. Unlike [#toPath()] this is cheap, it never reverses the states. Use it for
  /// paths which are kept for a long time, like transfers, so that the search states can be
  /// garbage collected. Use [StreetPath#reTraverse] to get a complete path again.
  StreetPath withoutStates();
}
