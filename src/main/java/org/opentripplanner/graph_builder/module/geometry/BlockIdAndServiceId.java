package org.opentripplanner.graph_builder.module.geometry;

import org.opentripplanner.transit.model.framework.FeedScopedId;
import org.opentripplanner.transit.model.timetable.Trip;

/**
 * This compound key object is used when grouping interlining trips together by (serviceId,
 * blockId).
 */
class BlockIdAndServiceId {

  String blockId;
  FeedScopedId serviceId;

  BlockIdAndServiceId(Trip trip) {
    this.blockId = trip.getGtfsBlockId();
    this.serviceId = trip.getServiceId();
  }

  @Override
  public int hashCode() {
    return blockId.hashCode() * 31 + serviceId.hashCode();
  }

  public boolean equals(Object o) {
    if (o instanceof BlockIdAndServiceId) {
      BlockIdAndServiceId other = ((BlockIdAndServiceId) o);
      return other.blockId.equals(blockId) && other.serviceId.equals(serviceId);
    }
    return false;
  }
}
