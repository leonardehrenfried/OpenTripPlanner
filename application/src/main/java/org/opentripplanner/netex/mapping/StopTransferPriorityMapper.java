package org.opentripplanner.netex.mapping;

import jakarta.annotation.Nullable;
import org.opentripplanner.transit.model.site.StopTransferPriority;
import org.rutebanken.netex.model.InterchangeWeightingEnumeration;

class StopTransferPriorityMapper {

  @Nullable
  static StopTransferPriority mapToDomain(InterchangeWeightingEnumeration value) {
    if (value == null) {
      return null;
    }

    return switch (value) {
      case NO_INTERCHANGE -> StopTransferPriority.DISCOURAGED;
      case INTERCHANGE_ALLOWED -> StopTransferPriority.ALLOWED;
      case PREFERRED_INTERCHANGE -> StopTransferPriority.PREFERRED;
      case RECOMMENDED_INTERCHANGE -> StopTransferPriority.RECOMMENDED;
    };
  }
}
