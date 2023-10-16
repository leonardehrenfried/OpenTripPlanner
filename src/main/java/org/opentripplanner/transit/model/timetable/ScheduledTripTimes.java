package org.opentripplanner.transit.model.timetable;

import static org.opentripplanner.transit.model.timetable.TimetableValidationError.ErrorCode.NEGATIVE_DWELL_TIME;
import static org.opentripplanner.transit.model.timetable.TimetableValidationError.ErrorCode.NEGATIVE_HOP_TIME;

import java.io.Serializable;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.opentripplanner.framework.i18n.I18NString;
import org.opentripplanner.framework.lang.IntUtils;
import org.opentripplanner.framework.time.DurationUtils;
import org.opentripplanner.model.BookingInfo;
import org.opentripplanner.transit.model.basic.Accessibility;
import org.opentripplanner.transit.model.framework.DataValidationException;
import org.opentripplanner.transit.model.framework.Deduplicator;
import org.opentripplanner.transit.model.framework.DeduplicatorService;

public final class ScheduledTripTimes implements Serializable, Comparable<ScheduledTripTimes> {

  /**
   * When time-shifting from one time-zone to another negative times may occur.
   */
  private static final int MIN_TIME = DurationUtils.durationInSeconds("-12h");
  /**
   * We allow a trip to last for maximum 10 days. In Norway the longest trip is 6 days.
   */
  private static final int MAX_TIME = DurationUtils.durationInSeconds("10d");

  /**
   * Implementation notes: This allows re-using the same scheduled arrival and departure time
   * arrays for many ScheduledTripTimes. It is also used in materializing frequency-based
   * ScheduledTripTimes.
   */
  private final int timeShift;
  private final int serviceCode;
  private final int[] arrivalTimes;
  private final int[] departureTimes;
  private final BitSet timepoints;
  private final Trip trip;
  private final List<BookingInfo> dropOffBookingInfos;
  private final List<BookingInfo> pickupBookingInfos;

  @Nullable
  private final I18NString[] headsigns;

  /**
   * Implementation notes: This is 2D array since there can be more than one via name/stop per each
   * record in stop sequence). Outer array may be null if there are no vias in stop sequence. Inner
   * array may be null if there are no vias for particular stop. This is done in order to save
   * space.
   */
  @Nullable
  private final String[][] headsignVias;

  private final int[] originalGtfsStopSequence;

  ScheduledTripTimes(ScheduledTripTimesBuilder builder) {
    this.timeShift = builder.timeShift();
    this.serviceCode = builder.serviceCode();
    this.arrivalTimes = Objects.requireNonNull(builder.arrivalTimes());
    this.departureTimes = Objects.requireNonNull(builder.departureTimes());
    this.timepoints = Objects.requireNonNull(builder.timepoints());
    this.trip = Objects.requireNonNull(builder.trip());
    this.pickupBookingInfos = Objects.requireNonNull(builder.pickupBookingInfos());
    this.dropOffBookingInfos = Objects.requireNonNull(builder.dropOffBookingInfos());
    this.headsigns = builder.headsigns();
    this.headsignVias = builder.headsignVias();
    this.originalGtfsStopSequence = builder.originalGtfsStopSequence();
    validate();
  }

  /**
   * Always provide a deduplicator when building the graph. No deduplication is ok when changing
   * simple fields like {@code timeShift} and {@code serviceCode} or even the prefered way in an
   * unittest.
   */
  public static ScheduledTripTimesBuilder of() {
    return new ScheduledTripTimesBuilder(null);
  }

  public static ScheduledTripTimesBuilder of(DeduplicatorService deduplicator) {
    return new ScheduledTripTimesBuilder(deduplicator);
  }

  public ScheduledTripTimesBuilder copyOf(Deduplicator deduplicator) {
    return new ScheduledTripTimesBuilder(
      timeShift,
      serviceCode,
      arrivalTimes,
      departureTimes,
      timepoints,
      trip,
      dropOffBookingInfos,
      pickupBookingInfos,
      headsigns,
      headsignVias,
      originalGtfsStopSequence,
      deduplicator
    );
  }

  /**
   * @see #copyOf(Deduplicator) copyOf(null)
   */
  public ScheduledTripTimesBuilder copyOfNoDuplication() {
    return copyOf(null);
  }

  /** The code for the service on which this trip runs. For departure search optimizations. */
  public int getServiceCode() {
    return serviceCode;
  }

  /**
   * The time in seconds after midnight at which the vehicle should arrive at the given stop
   * according to the original schedule.
   */
  public int getScheduledArrivalTime(final int stop) {
    return arrivalTimes[stop] + timeShift;
  }

  /**
   * The time in seconds after midnight at which the vehicle arrives at each stop, accounting for
   * any real-time updates.
   */
  public int getArrivalTime(final int stop) {
    return getScheduledArrivalTime(stop);
  }

  /** @return the difference between the scheduled and actual arrival times at this stop. */
  public int getArrivalDelay(final int stop) {
    return getArrivalTime(stop) - (arrivalTimes[stop] + timeShift);
  }

  /**
   * The time in seconds after midnight at which the vehicle should leave the given stop according
   * to the original schedule.
   */
  public int getScheduledDepartureTime(final int stop) {
    return departureTimes[stop] + timeShift;
  }

  /**
   * The time in seconds after midnight at which the vehicle leaves each stop, accounting for any
   * real-time updates.
   */
  public int getDepartureTime(final int stop) {
    return getScheduledDepartureTime(stop);
  }

  /** @return the difference between the scheduled and actual departure times at this stop. */
  public int getDepartureDelay(final int stop) {
    return getDepartureTime(stop) - (departureTimes[stop] + timeShift);
  }

  /**
   * Whether or not stopIndex is considered a GTFS timepoint.
   */
  public boolean isTimepoint(final int stopIndex) {
    return timepoints.get(stopIndex);
  }

  /** The trips whose arrivals and departures are represented by this class */
  public Trip getTrip() {
    return trip;
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

  public BookingInfo getDropOffBookingInfo(int stop) {
    return dropOffBookingInfos.get(stop);
  }

  public BookingInfo getPickupBookingInfo(int stop) {
    return pickupBookingInfos.get(stop);
  }

  /**
   * Return {@code true} if the trip is unmodified, a scheduled trip from a published timetable.
   * Return {@code false} if the trip is an updated, cancelled, or otherwise modified one. This
   * method differs from {@link #getRealTimeState()} in that it checks whether real-time
   * information is actually available.
   */
  public boolean isScheduled() {
    return true;
  }

  /**
   * Return {@code true} if canceled or soft-deleted
   */
  public boolean isCanceledOrDeleted() {
    return false;
  }

  /**
   * Return {@code true} if canceled
   */
  public boolean isCanceled() {
    return false;
  }

  /**
   * Return true if trip is soft-deleted, and should not be visible to the user
   */
  public boolean isDeleted() {
    return false;
  }

  public RealTimeState getRealTimeState() {
    return RealTimeState.SCHEDULED;
  }

  /**
   * @return the whole trip's headsign. Individual stops can have different headsigns.
   */
  public I18NString getTripHeadsign() {
    return trip.getHeadsign();
  }

  /**
   * Both trip_headsign and stop_headsign (per stop on a particular trip) are optional GTFS fields.
   * A trip may not have a headsign, in which case we should fall back on a Timetable or
   * Pattern-level headsign. Such a string will be available when we give TripPatterns or
   * StopPatterns unique human-readable route variant names, but a ScheduledTripTimes currently
   * does not have a pointer to its enclosing timetable or pattern.
   */
  @Nullable
  public I18NString getHeadsign(final int stop) {
    return (headsigns != null && headsigns[stop] != null)
      ? headsigns[stop]
      : getTrip().getHeadsign();
  }

  /**
   * Return list of via names per particular stop. This field provides info about intermediate stops
   * between current stop and final trip destination. Mapped from NeTEx DestinationDisplay.vias. No
   * GTFS mapping at the moment.
   *
   * @return Empty list if there are no vias registered for a stop.
   */
  public List<String> getHeadsignVias(final int stop) {
    if (headsignVias == null || headsignVias[stop] == null) {
      return List.of();
    }
    return List.of(headsignVias[stop]);
  }

  public int getNumStops() {
    return arrivalTimes.length;
  }

  public Accessibility getWheelchairAccessibility() {
    return trip.getWheelchairBoarding();
  }

  /**
   * This is only for API-purposes (does not affect routing).
   */
  public OccupancyStatus getOccupancyStatus(int ignore) {
    return OccupancyStatus.NO_DATA_AVAILABLE;
  }

  /** Sort trips based on first departure time. */
  @Override
  public int compareTo(final ScheduledTripTimes other) {
    return this.getDepartureTime(0) - other.getDepartureTime(0);
  }

  /**
   * Returns the GTFS sequence number of the given 0-based stop position.
   *
   * These are the GTFS stop sequence numbers, which show the order in which the vehicle visits the
   * stops. Despite the face that the StopPattern or TripPattern enclosing this class provides
   * an ordered list of Stops, the original stop sequence numbers may still be needed for matching
   * with GTFS-RT update messages. Unfortunately, each individual trip can have totally different
   * sequence numbers for the same stops, so we need to store them at the individual trip level. An
   * effort is made to re-use the sequence number arrays when they are the same across different
   * trips in the same pattern.
   */
  public int gtfsSequenceOfStopIndex(final int stop) {
    return originalGtfsStopSequence[stop];
  }

  /**
   * Returns the 0-based stop index of the given GTFS sequence number.
   */
  public OptionalInt stopIndexOfGtfsSequence(int stopSequence) {
    if (originalGtfsStopSequence == null) {
      return OptionalInt.empty();
    }
    for (int i = 0; i < originalGtfsStopSequence.length; i++) {
      var sequence = originalGtfsStopSequence[i];
      if (sequence == stopSequence) {
        return OptionalInt.of(i);
      }
    }
    return OptionalInt.empty();
  }

  @Override
  public boolean equals(Object o) {
    throw new UnsupportedOperationException("Not implemented, implement if needed!");
  }

  @Override
  public int hashCode() {
    throw new UnsupportedOperationException("Not implemented, implement if needed!");
  }

  /* package local - only visible to timetable classes */

  int[] copyArrivalTimes() {
    return IntUtils.shiftArray(timeShift, arrivalTimes);
  }

  int[] copyDepartureTimes() {
    return IntUtils.shiftArray(timeShift, departureTimes);
  }

  I18NString[] copyHeadsigns(Supplier<I18NString[]> defaultValue) {
    return headsigns == null ? defaultValue.get() : Arrays.copyOf(headsigns, headsigns.length);
  }

  /* private methods */

  private void validate() {
    int lastStop = departureTimes.length - 1;
    IntUtils.requireInRange(departureTimes[0], MIN_TIME, MAX_TIME);
    IntUtils.requireInRange(arrivalTimes[lastStop], MIN_TIME, MAX_TIME);
    validateNonIncreasingTimes();
  }

  /**
   * When creating scheduled trip times we could potentially imply negative running or dwell times.
   * We really don't want those being used in routing. This method checks that all times are
   * increasing. The first stop arrival time and the last stops depature time is NOT checked -
   * these should be ignored by raptor.
   */
  private void validateNonIncreasingTimes() {
    final int lastStop = arrivalTimes.length - 1;

    // This check is currently used since Flex trips may have only one stop. This class should
    // not be used to represent FLEX, so remove this check and create new data classes for FLEX
    // trips.
    if (lastStop < 1) {
      return;
    }
    int prevDep = getDepartureTime(0);

    for (int i = 1; true; ++i) {
      final int arr = getArrivalTime(i);
      final int dep = getDepartureTime(i);

      if (prevDep > arr) {
        throw new DataValidationException(new TimetableValidationError(NEGATIVE_HOP_TIME, i, trip));
      }
      if (i == lastStop) {
        return;
      }
      if (dep < arr) {
        throw new DataValidationException(
          new TimetableValidationError(NEGATIVE_DWELL_TIME, i, trip)
        );
      }
      prevDep = dep;
    }
  }
}
