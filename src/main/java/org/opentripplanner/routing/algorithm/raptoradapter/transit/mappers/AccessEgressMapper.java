package org.opentripplanner.routing.algorithm.raptoradapter.transit.mappers;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.opentripplanner.ext.flex.FlexAccessEgress;
import org.opentripplanner.model.Stop;
import org.opentripplanner.routing.algorithm.raptoradapter.transit.AccessEgress;
import org.opentripplanner.routing.algorithm.raptoradapter.transit.FlexAccessEgressAdapter;
import org.opentripplanner.routing.algorithm.raptoradapter.transit.StopIndexForRaptor;
import org.opentripplanner.routing.graphfinder.NearbyStop;

public class AccessEgressMapper {

  private final StopIndexForRaptor stopIndex;

  public AccessEgressMapper(StopIndexForRaptor stopIndex) {
    this.stopIndex = stopIndex;
  }

  public AccessEgress mapNearbyStop(NearbyStop nearbyStop, ZonedDateTime startOfTime, boolean isEgress) {
    if (!(nearbyStop.stop instanceof Stop)) {
      return null;
    }

    return new AccessEgress(
      stopIndex.indexOf(nearbyStop.stop),
        isEgress ? nearbyStop.state.reverse() : nearbyStop.state,
        startOfTime
    );
  }

  public List<AccessEgress> mapNearbyStops(Collection<NearbyStop> accessStops, ZonedDateTime startOfTime, boolean isEgress) {
    return accessStops
      .stream()
        .map(stopAtDistance -> mapNearbyStop(stopAtDistance, startOfTime, isEgress))
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }

  public Collection<AccessEgress> mapFlexAccessEgresses(
    Collection<FlexAccessEgress> flexAccessEgresses,
          ZonedDateTime startOfTime,
    boolean isEgress
  ) {
    return flexAccessEgresses
      .stream()
      .map(flexAccessEgress -> new FlexAccessEgressAdapter(flexAccessEgress, stopIndex, startOfTime, isEgress))
      .collect(Collectors.toList());
  }
}
