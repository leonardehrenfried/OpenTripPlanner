package org.opentripplanner.place.nearbystopfinder;

import java.util.List;
import java.util.Set;
import org.opentripplanner.place.api.NearbyStop;
import org.opentripplanner.street.api.path.StreetPath;
import org.opentripplanner.street.model.vertex.TransitStopVertex;

public class NearbyStopFactory {

  /**
   * Create zero distance NearbyStops given a list of TransitStopVertices
   */
  public static List<NearbyStop> nearbyStopsForTransitStopVertices(
    Set<TransitStopVertex> stopVertices
  ) {
    if (stopVertices.isEmpty()) {
      return List.of();
    }

    return stopVertices
      .stream()
      .map(s -> new NearbyStop(s.getId(), 0d, s, StreetPath.EMPTY))
      .toList();
  }
}
