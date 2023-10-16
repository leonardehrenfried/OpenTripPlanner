package org.opentripplanner.transit.model.timetable;

import java.util.BitSet;
import java.util.List;
import javax.annotation.Nullable;
import org.opentripplanner.framework.i18n.I18NString;
import org.opentripplanner.model.BookingInfo;
import org.opentripplanner.transit.model.framework.Deduplicator;
import org.opentripplanner.transit.model.framework.DeduplicatorService;

public class ScheduledTripTimesBuilder {

  private final int NOT_SET = -1;

  int[] arrivalTimes;
  int[] departureTimes;
  int timeShift;
  int serviceCode = NOT_SET;
  BitSet timepoints;
  List<BookingInfo> dropOffBookingInfos;
  List<BookingInfo> pickupBookingInfos;
  Trip trip;
  I18NString[] headsigns;
  String[][] headsignVias;
  int[] originalGtfsStopSequence;

  private final DeduplicatorService deduplicator;

  ScheduledTripTimesBuilder(@Nullable DeduplicatorService deduplicator) {
    this.deduplicator = deduplicator == null ? DeduplicatorService.NOOP : deduplicator;
  }

  ScheduledTripTimesBuilder(
    int[] arrivalTimes,
    int[] departureTimes,
    int timeShift,
    int serviceCode,
    BitSet timepoints,
    List<BookingInfo> dropOffBookingInfos,
    List<BookingInfo> pickupBookingInfos,
    Trip trip,
    I18NString[] headsigns,
    String[][] headsignVias,
    int[] originalGtfsStopSequence,
    Deduplicator deduplicator
  ) {
    this(deduplicator);
    this.arrivalTimes = arrivalTimes;
    this.departureTimes = departureTimes;
    this.timeShift = timeShift;
    this.serviceCode = serviceCode;
    this.timepoints = timepoints;
    this.dropOffBookingInfos = dropOffBookingInfos;
    this.pickupBookingInfos = pickupBookingInfos;
    this.trip = trip;
    this.headsigns = headsigns;
    this.headsignVias = headsignVias;
    this.originalGtfsStopSequence = originalGtfsStopSequence;
  }

  public ScheduledTripTimesBuilder withArrivalTimes(int[] arrivalTimes) {
    this.arrivalTimes = deduplicator.deduplicateIntArray(arrivalTimes);
    return this;
  }

  public ScheduledTripTimesBuilder withDepartureTimes(int[] departureTimes) {
    this.departureTimes = deduplicator.deduplicateIntArray(departureTimes);
    return this;
  }

  public ScheduledTripTimesBuilder withTimeShift(int timeShift) {
    this.timeShift = timeShift;
    return this;
  }

  public ScheduledTripTimesBuilder withServiceCode(int serviceCode) {
    this.serviceCode = serviceCode;
    return this;
  }

  public ScheduledTripTimesBuilder withTimepoints(BitSet timepoints) {
    this.timepoints = deduplicator.deduplicateBitSet(timepoints);
    return this;
  }

  public ScheduledTripTimesBuilder withDropOffBookingInfos(List<BookingInfo> dropOffBookingInfos) {
    this.dropOffBookingInfos =
      deduplicator.deduplicateImmutableList(BookingInfo.class, dropOffBookingInfos);
    return this;
  }

  public ScheduledTripTimesBuilder withPickupBookingInfos(List<BookingInfo> pickupBookingInfos) {
    this.pickupBookingInfos =
      deduplicator.deduplicateImmutableList(BookingInfo.class, pickupBookingInfos);
    return this;
  }

  public ScheduledTripTimesBuilder withTrip(Trip trip) {
    this.trip = trip;
    return this;
  }

  public ScheduledTripTimesBuilder withHeadsigns(I18NString[] headsigns) {
    this.headsigns = deduplicator.deduplicateObjectArray(I18NString.class, headsigns);
    return this;
  }

  public ScheduledTripTimesBuilder withHeadsignVias(String[][] headsignVias) {
    this.headsignVias = deduplicator.deduplicateString2DArray(headsignVias);
    return this;
  }

  public ScheduledTripTimesBuilder withOriginalGtfsStopSequence(int[] originalGtfsStopSequence) {
    this.originalGtfsStopSequence = deduplicator.deduplicateIntArray(originalGtfsStopSequence);
    return this;
  }

  public ScheduledTripTimes build() {
    return new ScheduledTripTimes(this);
  }
}
