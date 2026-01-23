package org.opentripplanner.updater.trip.gtfs.moduletests.delay;

import static org.opentripplanner.updater.spi.UpdateResultAssertions.assertSuccess;

import org.junit.jupiter.api.Test;
import org.opentripplanner.transit.model._data.TransitTestEnvironment;
import org.opentripplanner.transit.model._data.TransitTestEnvironmentBuilder;
import org.opentripplanner.transit.model._data.TripInput;
import org.opentripplanner.transit.model.site.RegularStop;
import org.opentripplanner.updater.trip.GtfsRtTestHelper;
import org.opentripplanner.updater.trip.RealtimeTestConstants;

class NoArrivalOnFirstStopTest implements RealtimeTestConstants {

  private final TransitTestEnvironmentBuilder ENV_BUILDER = TransitTestEnvironment.of();
  private final RegularStop STOP_A = ENV_BUILDER.stop(STOP_A_ID);
  private final RegularStop STOP_B = ENV_BUILDER.stop(STOP_B_ID);

  @Test
  void noArrivalOnFirstStop() {
    var tripInput = TripInput.of(TRIP_1_ID)
      .addStop(STOP_A, "10:01", "10:01")
      .addStop(STOP_B, "10:02", "10:02");
    var env = ENV_BUILDER.addTrip(tripInput).build();
    var rt = GtfsRtTestHelper.of(env);

    var tripUpdate = rt
      .tripUpdateScheduled(TRIP_1_ID)
      .addStopTimeWithArrivalAndDeparture(0, null, "10:01:30")
      .build();

    assertSuccess(rt.applyTripUpdate(tripUpdate));
  }
}
