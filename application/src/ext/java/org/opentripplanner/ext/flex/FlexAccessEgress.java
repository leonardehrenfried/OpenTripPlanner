package org.opentripplanner.ext.flex;

import static org.opentripplanner.model.StopTime.MISSING_VALUE;

import java.util.Objects;
import org.opentripplanner.ext.flex.trip.FlexTrip;
import org.opentripplanner.street.model.path.StreetPath;
import org.opentripplanner.transit.model.site.RegularStop;
import org.opentripplanner.transit.model.timetable.booking.RoutingBookingInfo;
import org.opentripplanner.utils.tostring.ToStringBuilder;

public final class FlexAccessEgress {

  private final RegularStop stop;
  private final FlexPathDurations pathDurations;
  private final int boardStopPosition;
  private final int alightStopPosition;
  private final FlexTrip<?, ?> trip;
  private final StreetPath path;
  private final boolean stopReachedOnBoard;
  private final RoutingBookingInfo routingBookingInfo;

  public FlexAccessEgress(
    RegularStop stop,
    FlexPathDurations pathDurations,
    int boardStopPosition,
    int alightStopPosition,
    FlexTrip<?, ?> trip,
    StreetPath path,
    boolean stopReachedOnBoard,
    int requestedBookingTime
  ) {
    this.stop = stop;
    this.pathDurations = pathDurations;
    this.boardStopPosition = boardStopPosition;
    this.alightStopPosition = alightStopPosition;
    this.trip = Objects.requireNonNull(trip);
    this.path = Objects.requireNonNull(path);
    this.stopReachedOnBoard = stopReachedOnBoard;
    this.routingBookingInfo = RoutingBookingInfo.of(
      requestedBookingTime,
      trip.getPickupBookingInfo(boardStopPosition)
    );
  }

  public RegularStop stop() {
    return stop;
  }

  /**
   * The street path of the whole access/egress, including the flex trip.
   */
  public StreetPath path() {
    return path;
  }

  public boolean stopReachedOnBoard() {
    return stopReachedOnBoard;
  }

  public int earliestDepartureTime(int departureTime) {
    int tripDepartureTime = pathDurations.mapToFlexTripDepartureTime(departureTime);

    // Apply minimum-booking-notice
    tripDepartureTime = routingBookingInfo.earliestDepartureTime(tripDepartureTime);

    int earliestDepartureTime = trip.earliestDepartureTime(
      tripDepartureTime,
      boardStopPosition,
      alightStopPosition,
      pathDurations.trip()
    );
    if (earliestDepartureTime == MISSING_VALUE) {
      return MISSING_VALUE;
    }
    return pathDurations.mapToRouterDepartureTime(earliestDepartureTime);
  }

  public int latestArrivalTime(int arrivalTime) {
    int tripArrivalTime = pathDurations.mapToFlexTripArrivalTime(arrivalTime);
    int latestArrivalTime = trip.latestArrivalTime(
      tripArrivalTime,
      boardStopPosition,
      alightStopPosition,
      pathDurations.trip()
    );
    if (latestArrivalTime == MISSING_VALUE) {
      return MISSING_VALUE;
    }
    if (routingBookingInfo.exceedsMinimumBookingNotice(latestArrivalTime - pathDurations.trip())) {
      return MISSING_VALUE;
    }
    return pathDurations.mapToRouterArrivalTime(latestArrivalTime);
  }

  @Override
  public String toString() {
    return ToStringBuilder.of(FlexAccessEgress.class)
      .addNum("boardStopPosition", boardStopPosition)
      .addNum("alightStopPosition", alightStopPosition)
      .addObj("durations", pathDurations)
      .addObj("stop", stop)
      .addObj("trip", trip.getId())
      .addObj("path", path)
      .addBoolIfTrue("stopReachedOnBoard", stopReachedOnBoard)
      .toString();
  }
}
