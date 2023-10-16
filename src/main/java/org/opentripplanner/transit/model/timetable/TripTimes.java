package org.opentripplanner.transit.model.timetable;

import static org.opentripplanner.transit.model.timetable.ValidationError.ErrorCode.NEGATIVE_DWELL_TIME;
import static org.opentripplanner.transit.model.timetable.ValidationError.ErrorCode.NEGATIVE_HOP_TIME;

import java.io.Serializable;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.IntUnaryOperator;
import javax.annotation.Nullable;
import org.opentripplanner.framework.i18n.I18NString;
import org.opentripplanner.model.BookingInfo;
import org.opentripplanner.transit.model.basic.Accessibility;

/**
 * A TripTimes represents the arrival and departure times for a single trip in an Timetable. It is
 * carried along by States when routing to ensure that they have a consistent, fast view of the trip
 * when realtime updates have been applied. All times are expressed as seconds since midnight (as in
 * GTFS).
 */
public final class TripTimes implements Serializable, Comparable<TripTimes> {

  private final ScheduledTripTimes scheduledTripTimes;

  private int[] arrivalTimes;
  private int[] departureTimes;
  private RealTimeState realTimeState;
  private StopRealTimeState[] stopRealTimeStates;
  private I18NString[] headsigns;
  private OccupancyStatus[] occupancyStatus;
  private Accessibility wheelchairAccessibility;

  private TripTimes(final TripTimes original, int timeShiftDelta) {
    this(original, new ScheduledTripTimes(original.scheduledTripTimes, timeShiftDelta));
  }

  TripTimes(ScheduledTripTimes scheduledTripTimes) {
    this(
      scheduledTripTimes,
      scheduledTripTimes.getRealTimeState(),
      null,
      null,
      null,
      scheduledTripTimes.getWheelchairAccessibility()
    );
  }

  private TripTimes(TripTimes original, ScheduledTripTimes scheduledTripTimes) {
    this(
      scheduledTripTimes,
      original.realTimeState,
      original.stopRealTimeStates,
      original.headsigns,
      original.occupancyStatus,
      original.wheelchairAccessibility
    );
  }

  private TripTimes(
    ScheduledTripTimes scheduledTripTimes,
    RealTimeState realTimeState,
    StopRealTimeState[] stopRealTimeStates,
    I18NString[] headsigns,
    OccupancyStatus[] occupancyStatus,
    Accessibility wheelchairAccessibility
  ) {
    this.scheduledTripTimes = scheduledTripTimes;
    this.realTimeState = realTimeState;
    this.stopRealTimeStates = stopRealTimeStates;
    this.headsigns = headsigns;
    this.occupancyStatus = occupancyStatus;
    this.wheelchairAccessibility = wheelchairAccessibility;

    // We set these to null to indicate that this is a non-updated/scheduled TripTimes.
    // We cannot point to the scheduled times because we do not want to make an unnecessary copy.
    this.arrivalTimes = null;
    this.departureTimes = null;
  }

  public static TripTimes of(ScheduledTripTimes scheduledTripTimes) {
    return new TripTimes(scheduledTripTimes);
  }

  /**
   * Copy scheduled times, but not the actual times.
   */
  public TripTimes copyOfScheduledTimes() {
    return new TripTimes(this, scheduledTripTimes);
  }

  /**
   * Both trip_headsign and stop_headsign (per stop on a particular trip) are optional GTFS fields.
   * A trip may not have a headsign, in which case we should fall back on a Timetable or
   * Pattern-level headsign. Such a string will be available when we give TripPatterns or
   * StopPatterns unique human readable route variant names, but a TripTimes currently does not have
   * a pointer to its enclosing timetable or pattern.
   */
  @Nullable
  public I18NString getHeadsign(final int stop) {
    return (headsigns != null && headsigns[stop] != null)
      ? headsigns[stop]
      : scheduledTripTimes.getHeadsign(stop);
  }

  /**
   * Return list of via names per particular stop. This field provides info about intermediate stops
   * between current stop and final trip destination. Mapped from NeTEx DestinationDisplay.vias. No
   * GTFS mapping at the moment.
   *
   * @return Empty list if there are no vias registered for a stop.
   */
  public List<String> getHeadsignVias(final int stop) {
    return scheduledTripTimes.getHeadsignVias(stop);
  }

  /**
   * @return the whole trip's headsign. Individual stops can have different headsigns.
   */
  public I18NString getTripHeadsign() {
    return scheduledTripTimes.getTripHeadsign();
  }

  /**
   * The time in seconds after midnight at which the vehicle should arrive at the given stop
   * according to the original schedule.
   */
  public int getScheduledArrivalTime(final int stop) {
    return scheduledTripTimes.getScheduledArrivalTime(stop);
  }

  /**
   * The time in seconds after midnight at which the vehicle should leave the given stop according
   * to the original schedule.
   */
  public int getScheduledDepartureTime(final int stop) {
    return scheduledTripTimes.getScheduledDepartureTime(stop);
  }

  /**
   * Return an integer which can be used to sort TripTimes in order of departure/arrivals.
   * <p>
   * This sorted trip times is used to search for trips. OTP assume one trip do NOT pass another
   * trip down the line.
   */
  public int sortIndex() {
    return getDepartureTime(0);
  }

  /**
   * The time in seconds after midnight at which the vehicle arrives at each stop, accounting for
   * any real-time updates.
   */
  public int getArrivalTime(final int stop) {
    return getOrElse(stop, arrivalTimes, scheduledTripTimes::getScheduledArrivalTime);
  }

  /**
   * The time in seconds after midnight at which the vehicle leaves each stop, accounting for any
   * real-time updates.
   */
  public int getDepartureTime(final int stop) {
    return getOrElse(stop, departureTimes, scheduledTripTimes::getScheduledDepartureTime);
  }

  /** @return the difference between the scheduled and actual arrival times at this stop. */
  public int getArrivalDelay(final int stop) {
    return getArrivalTime(stop) - scheduledTripTimes.getScheduledArrivalTime(stop);
  }

  /** @return the difference between the scheduled and actual departure times at this stop. */
  public int getDepartureDelay(final int stop) {
    return getDepartureTime(stop) - scheduledTripTimes.getScheduledDepartureTime(stop);
  }

  public void setRecorded(int stop) {
    setStopRealTimeStates(stop, StopRealTimeState.RECORDED);
  }

  public void setCancelled(int stop) {
    setStopRealTimeStates(stop, StopRealTimeState.CANCELLED);
  }

  public void setNoData(int stop) {
    setStopRealTimeStates(stop, StopRealTimeState.NO_DATA);
  }

  public void setPredictionInaccurate(int stop) {
    setStopRealTimeStates(stop, StopRealTimeState.INACCURATE_PREDICTIONS);
  }

  public boolean isCancelledStop(int stop) {
    return isStopRealTimeStates(stop, StopRealTimeState.CANCELLED);
  }

  public boolean isRecordedStop(int stop) {
    return isStopRealTimeStates(stop, StopRealTimeState.RECORDED);
  }

  public boolean isNoDataStop(int stop) {
    return isStopRealTimeStates(stop, StopRealTimeState.NO_DATA);
  }

  public boolean isPredictionInaccurate(int stop) {
    return isStopRealTimeStates(stop, StopRealTimeState.INACCURATE_PREDICTIONS);
  }

  public void setOccupancyStatus(int stop, OccupancyStatus occupancyStatus) {
    prepareForRealTimeUpdates();
    this.occupancyStatus[stop] = occupancyStatus;
  }

  /**
   * This is only for API-purposes (does not affect routing).
   */
  public OccupancyStatus getOccupancyStatus(int stop) {
    if (this.occupancyStatus == null) {
      return OccupancyStatus.NO_DATA_AVAILABLE;
    }
    return this.occupancyStatus[stop];
  }

  public BookingInfo getDropOffBookingInfo(int stop) {
    return scheduledTripTimes.getDropOffBookingInfo(stop);
  }

  public BookingInfo getPickupBookingInfo(int stop) {
    return scheduledTripTimes.getPickupBookingInfo(stop);
  }

  /**
   * Return {@code true} if the trip is unmodified, a scheduled trip from a published timetable.
   * Return {@code false} if the trip is an updated, cancelled, or otherwise modified one. This
   * method differs from {@link #getRealTimeState()} in that it checks whether real-time
   * information is actually available.
   */
  public boolean isScheduled() {
    return realTimeState == RealTimeState.SCHEDULED;
  }

  /**
   * Return {@code true} if canceled or soft-deleted
   */
  public boolean isCanceledOrDeleted() {
    return isCanceled() || isDeleted();
  }

  /**
   * Return {@code true} if canceled
   */
  public boolean isCanceled() {
    return realTimeState == RealTimeState.CANCELED;
  }

  /**
   * Return true if trip is soft-deleted, and should not be visible to the user
   */
  public boolean isDeleted() {
    return realTimeState == RealTimeState.DELETED;
  }

  public RealTimeState getRealTimeState() {
    return realTimeState;
  }

  public void setRealTimeState(final RealTimeState realTimeState) {
    this.realTimeState = realTimeState;
  }

  /**
   * When creating a scheduled TripTimes or wrapping it in updates, we could potentially imply
   * negative running or dwell times. We really don't want those being used in routing. This method
   * checks that all times are increasing.
   *
   * @return empty if times were found to be increasing, stop index of the first error otherwise
   */
  public Optional<ValidationError> validateNonIncreasingTimes() {
    final int nStops = arrivalTimes.length;
    int prevDep = -9_999_999;
    for (int s = 0; s < nStops; s++) {
      final int arr = getArrivalTime(s);
      final int dep = getDepartureTime(s);

      if (dep < arr) {
        return Optional.of(new ValidationError(NEGATIVE_DWELL_TIME, s));
      }
      if (prevDep > arr) {
        return Optional.of(new ValidationError(NEGATIVE_HOP_TIME, s));
      }
      prevDep = dep;
    }
    return Optional.empty();
  }

  /** Cancel this entire trip */
  public void cancelTrip() {
    realTimeState = RealTimeState.CANCELED;
  }

  /** Soft delete the entire trip */
  public void deleteTrip() {
    realTimeState = RealTimeState.DELETED;
  }

  public void updateDepartureTime(final int stop, final int time) {
    prepareForRealTimeUpdates();
    departureTimes[stop] = time;
  }

  public void updateDepartureDelay(final int stop, final int delay) {
    prepareForRealTimeUpdates();
    departureTimes[stop] = scheduledTripTimes.getScheduledDepartureTime(stop) + delay;
  }

  public void updateArrivalTime(final int stop, final int time) {
    prepareForRealTimeUpdates();
    arrivalTimes[stop] = time;
  }

  public void updateArrivalDelay(final int stop, final int delay) {
    prepareForRealTimeUpdates();
    arrivalTimes[stop] = scheduledTripTimes.getScheduledArrivalTime(stop) + delay;
  }

  @Nullable
  public Accessibility getWheelchairAccessibility() {
    // No need to fall back to scheduled state, since it is copied over in the constructor
    return wheelchairAccessibility;
  }

  public void updateWheelchairAccessibility(Accessibility wheelchairAccessibility) {
    this.wheelchairAccessibility = wheelchairAccessibility;
  }

  public int getNumStops() {
    return scheduledTripTimes.getNumStops();
  }

  /** Sort trips based on first departure time. */
  @Override
  public int compareTo(final TripTimes other) {
    return this.getDepartureTime(0) - other.getDepartureTime(0);
  }

  /**
   * Returns a time-shifted copy of this TripTimes in which the vehicle passes the given stop index
   * (not stop sequence number) at the given time. We only have a mechanism to shift the scheduled
   * stoptimes, not the real-time stoptimes. Therefore, this only works on trips without updates for
   * now (frequency trips don't have updates).
   */
  public TripTimes timeShift(final int stop, final int time, final boolean depart) {
    if (arrivalTimes != null || departureTimes != null) {
      return null;
    }
    // Adjust 0-based times to match desired stoptime.
    final int shift = time - (depart ? getDepartureTime(stop) : getArrivalTime(stop));

    return new TripTimes(this, shift);
  }

  /**
   * Time-shift all times on this trip. This is used when updating the time zone for the trip.
   */
  public TripTimes adjustTimesToGraphTimeZone(Duration duration) {
    return new TripTimes(this, (int) duration.toSeconds());
  }

  /**
   * Returns the GTFS sequence number of the given 0-based stop position.
   *
   * These are the GTFS stop sequence numbers, which show the order in which the vehicle visits the
   * stops. Despite the face that the StopPattern or TripPattern enclosing this TripTimes provides
   * an ordered list of Stops, the original stop sequence numbers may still be needed for matching
   * with GTFS-RT update messages. Unfortunately, each individual trip can have totally different
   * sequence numbers for the same stops, so we need to store them at the individual trip level. An
   * effort is made to re-use the sequence number arrays when they are the same across different
   * trips in the same pattern.
   */
  public int gtfsSequenceOfStopIndex(final int stop) {
    return scheduledTripTimes.gtfsSequenceOfStopIndex(stop);
  }

  /**
   * Returns the 0-based stop index of the given GTFS sequence number.
   */
  public OptionalInt stopIndexOfGtfsSequence(int stopSequence) {
    return scheduledTripTimes.stopIndexOfGtfsSequence(stopSequence);
  }

  /**
   * Whether or not stopIndex is considered a GTFS timepoint.
   */
  public boolean isTimepoint(final int stopIndex) {
    return scheduledTripTimes.isTimepoint(stopIndex);
  }

  /** The code for the service on which this trip runs. For departure search optimizations. */
  public int getServiceCode() {
    return scheduledTripTimes.getServiceCode();
  }

  public void setServiceCode(int serviceCode) {
    scheduledTripTimes.setServiceCode(serviceCode);
  }

  /** The trips whose arrivals and departures are represented by this class */
  public Trip getTrip() {
    return scheduledTripTimes.getTrip();
  }

  /**
   * Adjusts arrival time for the stop at the firstUpdatedIndex if no update was given for it and
   * arrival/departure times for the stops before that stop. Returns {@code true} if times have been
   * adjusted.
   */
  public boolean adjustTimesBeforeAlways(int firstUpdatedIndex) {
    boolean hasAdjustedTimes = false;
    int delay = getDepartureDelay(firstUpdatedIndex);
    if (getArrivalDelay(firstUpdatedIndex) == 0) {
      updateArrivalDelay(firstUpdatedIndex, delay);
      hasAdjustedTimes = true;
    }
    delay = getArrivalDelay(firstUpdatedIndex);
    if (delay == 0) {
      return false;
    }
    for (int i = firstUpdatedIndex - 1; i >= 0; i--) {
      hasAdjustedTimes = true;
      updateDepartureDelay(i, delay);
      updateArrivalDelay(i, delay);
    }
    return hasAdjustedTimes;
  }

  /**
   * Adjusts arrival and departure times for the stops before the stop at firstUpdatedIndex when
   * required to ensure that the times are increasing. Can set NO_DATA flag on the updated previous
   * stops. Returns {@code true} if times have been adjusted.
   */
  public boolean adjustTimesBeforeWhenRequired(int firstUpdatedIndex, boolean setNoData) {
    if (getArrivalTime(firstUpdatedIndex) > getDepartureTime(firstUpdatedIndex)) {
      // The given trip update has arrival time after departure time for the first updated stop.
      // This method doesn't try to fix issues in the given data, only for the missing part
      return false;
    }
    int nextStopArrivalTime = getArrivalTime(firstUpdatedIndex);
    int delay = getArrivalDelay(firstUpdatedIndex);
    boolean hasAdjustedTimes = false;
    boolean adjustTimes = true;
    for (int i = firstUpdatedIndex - 1; i >= 0; i--) {
      if (setNoData && !isCancelledStop(i)) {
        setNoData(i);
      }
      if (adjustTimes) {
        if (getDepartureTime(i) < nextStopArrivalTime) {
          adjustTimes = false;
          continue;
        } else {
          hasAdjustedTimes = true;
          updateDepartureDelay(i, delay);
        }
        if (getArrivalTime(i) < getDepartureTime(i)) {
          adjustTimes = false;
        } else {
          updateArrivalDelay(i, delay);
          nextStopArrivalTime = getArrivalTime(i);
        }
      }
    }
    return hasAdjustedTimes;
  }

  /* private member methods */

  private void setStopRealTimeStates(int stop, StopRealTimeState state) {
    prepareForRealTimeUpdates();
    this.stopRealTimeStates[stop] = state;
  }

  /**
   * The real-time states for a given stops. If the state is DEFAULT for a stop,
   * the {@link #getRealTimeState()} should determine the realtime state of the stop.
   * <p>
   * This is only for API-purposes (does not affect routing).
   */
  private boolean isStopRealTimeStates(int stop, StopRealTimeState state) {
    return stopRealTimeStates != null && stopRealTimeStates[stop] == state;
  }

  public void setHeadsign(int index, I18NString headsign) {
    if (headsigns == null) {
      if (headsign.equals(getTrip().getHeadsign())) {
        return;
      }
      this.headsigns = scheduledTripTimes.copyHeadsigns(() -> new I18NString[getNumStops()]);
      this.headsigns[index] = headsign;
      return;
    }

    prepareForRealTimeUpdates();
    headsigns[index] = headsign;
  }

  private static int getOrElse(int index, int[] array, IntUnaryOperator defaultValue) {
    return array != null ? array[index] : defaultValue.applyAsInt(index);
  }

  /**
   * If they don't already exist, create arrays for updated arrival and departure times that are
   * just time-shifted copies of the zero-based scheduled departure times.
   * <p>
   * Also sets the realtime state to UPDATED.
   */
  private void prepareForRealTimeUpdates() {
    if (arrivalTimes == null) {
      this.arrivalTimes = scheduledTripTimes.copyArrivalTimes();
      this.departureTimes = scheduledTripTimes.copyDepartureTimes();
      // Update the real-time state
      this.realTimeState = RealTimeState.UPDATED;
      this.stopRealTimeStates = new StopRealTimeState[arrivalTimes.length];
      Arrays.fill(stopRealTimeStates, StopRealTimeState.DEFAULT);
      this.headsigns = scheduledTripTimes.copyHeadsigns(() -> null);
      this.occupancyStatus = new OccupancyStatus[arrivalTimes.length];
      Arrays.fill(occupancyStatus, OccupancyStatus.NO_DATA_AVAILABLE);
      // skip immutable types: scheduledTripTimes & wheelchairAccessibility
    }
  }
}
