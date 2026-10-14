package org.opentripplanner.ext.carpooling.util;

import java.time.Duration;
import org.jspecify.annotations.Nullable;
import org.opentripplanner.street.model.StreetMode;
import org.opentripplanner.street.model.path.StreetLeg;
import org.opentripplanner.street.model.path.StreetPath;
import org.opentripplanner.street.search.request.StreetSearchRequest;

/**
 * Helpers for reading the duration, A* weight and distance of {@link StreetPath} segments, where a
 * missing segment counts as zero, and for replaying a walk with another request's preferences.
 */
public final class StreetPathUtils {

  /**
   * The paths are only used for their distance, so the elevation offset does not matter.
   */
  private static final double NO_ELEVATION_OFFSET = 0;

  private StreetPathUtils() {}

  /**
   * Returns the duration of the given path, or {@link Duration#ZERO} if the path is {@code null}.
   */
  public static Duration durationOrZero(@Nullable StreetPath path) {
    return path == null ? Duration.ZERO : path.duration();
  }

  /**
   * Returns the A* weight of the given path, or {@code 0} if the path is {@code null}. The weight
   * already accounts for the user's walk preferences (reluctance, safety factor, slope cost, etc.)
   * since it comes from the search that produced the path.
   */
  public static double weightOrZero(@Nullable StreetPath path) {
    return path == null ? 0 : path.generalizedCost();
  }

  /**
   * Returns the distance of the given path in meters, the sum of the distances of its edges.
   */
  public static double distanceMeters(StreetPath path) {
    return path
      .computeLegs(NO_ELEVATION_OFFSET)
      .stream()
      .mapToDouble(StreetLeg::distanceMeters)
      .sum();
  }

  /**
   * The same walk timed and weighted with {@code request}'s preferences: drives the path's edges
   * through the real traversals from its first vertex, no search involved. {@code null} when an
   * edge cannot be traversed under the request, for instance by a wheelchair user.
   */
  @Nullable
  public static StreetPath replay(StreetPath path, StreetSearchRequest request) {
    var walkRequest = StreetSearchRequest.copyOf(request)
      .withMode(StreetMode.WALK)
      .withArriveBy(false)
      .build();
    return path.reTraverse(walkRequest).orElse(null);
  }
}
