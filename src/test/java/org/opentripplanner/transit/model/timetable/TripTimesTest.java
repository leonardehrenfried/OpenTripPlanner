package org.opentripplanner.transit.model.timetable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.opentripplanner.transit.model._data.TransitModelForTest.id;
import static org.opentripplanner.transit.model.timetable.ValidationError.ErrorCode.NEGATIVE_DWELL_TIME;
import static org.opentripplanner.transit.model.timetable.ValidationError.ErrorCode.NEGATIVE_HOP_TIME;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.opentripplanner.framework.i18n.I18NString;
import org.opentripplanner.framework.i18n.NonLocalizedString;
import org.opentripplanner.model.StopTime;
import org.opentripplanner.transit.model._data.TransitModelForTest;
import org.opentripplanner.transit.model.framework.Deduplicator;
import org.opentripplanner.transit.model.framework.FeedScopedId;
import org.opentripplanner.transit.model.site.RegularStop;

class TripTimesTest {

  private static final String TRIP_ID = "testTripId";

  private static final List<FeedScopedId> stopIds = List.of(
    id("A"),
    id("B"),
    id("C"),
    id("D"),
    id("E"),
    id("F"),
    id("G"),
    id("H")
  );

  static TripTimes createInitialTripTimes() {
    Trip trip = TransitModelForTest.trip(TRIP_ID).build();

    List<StopTime> stopTimes = new LinkedList<>();

    for (int i = 0; i < stopIds.size(); ++i) {
      StopTime stopTime = new StopTime();

      RegularStop stop = TransitModelForTest.stopForTest(stopIds.get(i).getId(), 0.0, 0.0);
      stopTime.setStop(stop);
      stopTime.setArrivalTime(i * 60);
      stopTime.setDepartureTime(i * 60);
      stopTime.setStopSequence(i * 10);
      stopTimes.add(stopTime);
    }

    return TripTimesFactory.tripTimes(trip, stopTimes, new Deduplicator());
  }

  @Nested
  class Headsign {

    private static final NonLocalizedString STOP_TEST_DIRECTION = new NonLocalizedString(
      "STOP TEST DIRECTION"
    );
    private static final NonLocalizedString DIRECTION = new NonLocalizedString("DIRECTION");
    private static final StopTime EMPTY_STOPPOINT = new StopTime();

    @Test
    void shouldHandleBothNullScenario() {
      Trip trip = TransitModelForTest.trip("TRIP").build();
      List<StopTime> stopTimes = List.of(EMPTY_STOPPOINT, EMPTY_STOPPOINT, EMPTY_STOPPOINT);

      TripTimes tripTimes = TripTimesFactory.tripTimes(trip, stopTimes, new Deduplicator());

      I18NString headsignFirstStop = tripTimes.getHeadsign(0);
      assertNull(headsignFirstStop);
    }

    @Test
    void shouldHandleTripOnlyHeadSignScenario() {
      Trip trip = TransitModelForTest.trip("TRIP").withHeadsign(DIRECTION).build();
      List<StopTime> stopTimes = List.of(EMPTY_STOPPOINT, EMPTY_STOPPOINT, EMPTY_STOPPOINT);

      TripTimes tripTimes = TripTimesFactory.tripTimes(trip, stopTimes, new Deduplicator());

      I18NString headsignFirstStop = tripTimes.getHeadsign(0);
      assertEquals(DIRECTION, headsignFirstStop);
    }

    @Test
    void shouldHandleStopsOnlyHeadSignScenario() {
      Trip trip = TransitModelForTest.trip("TRIP").build();
      StopTime stopWithHeadsign = new StopTime();
      stopWithHeadsign.setStopHeadsign(STOP_TEST_DIRECTION);
      List<StopTime> stopTimes = List.of(stopWithHeadsign, stopWithHeadsign, stopWithHeadsign);

      TripTimes tripTimes = TripTimesFactory.tripTimes(trip, stopTimes, new Deduplicator());

      I18NString headsignFirstStop = tripTimes.getHeadsign(0);
      assertEquals(STOP_TEST_DIRECTION, headsignFirstStop);
    }

    @Test
    void shouldHandleStopsEqualToTripHeadSignScenario() {
      Trip trip = TransitModelForTest.trip("TRIP").withHeadsign(DIRECTION).build();
      StopTime stopWithHeadsign = new StopTime();
      stopWithHeadsign.setStopHeadsign(DIRECTION);
      List<StopTime> stopTimes = List.of(stopWithHeadsign, stopWithHeadsign, stopWithHeadsign);

      TripTimes tripTimes = TripTimesFactory.tripTimes(trip, stopTimes, new Deduplicator());

      I18NString headsignFirstStop = tripTimes.getHeadsign(0);
      assertEquals(DIRECTION, headsignFirstStop);
    }

    @Test
    void shouldHandleDifferingTripAndStopHeadSignScenario() {
      Trip trip = TransitModelForTest.trip("TRIP").withHeadsign(DIRECTION).build();
      StopTime stopWithHeadsign = new StopTime();
      stopWithHeadsign.setStopHeadsign(STOP_TEST_DIRECTION);
      List<StopTime> stopTimes = List.of(stopWithHeadsign, EMPTY_STOPPOINT, EMPTY_STOPPOINT);

      TripTimes tripTimes = TripTimesFactory.tripTimes(trip, stopTimes, new Deduplicator());

      I18NString headsignFirstStop = tripTimes.getHeadsign(0);
      assertEquals(STOP_TEST_DIRECTION, headsignFirstStop);

      I18NString headsignSecondStop = tripTimes.getHeadsign(1);
      assertEquals(DIRECTION, headsignSecondStop);
    }
  }

  @Test
  public void testStopUpdate() {
    TripTimes updatedTripTimesA = createInitialTripTimes().copyOfScheduledTimes();

    updatedTripTimesA.updateArrivalTime(3, 190);
    updatedTripTimesA.updateDepartureTime(3, 190);
    updatedTripTimesA.updateArrivalTime(5, 311);
    updatedTripTimesA.updateDepartureTime(5, 312);

    assertEquals(3 * 60 + 10, updatedTripTimesA.getArrivalTime(3));
    assertEquals(3 * 60 + 10, updatedTripTimesA.getDepartureTime(3));
    assertEquals(5 * 60 + 11, updatedTripTimesA.getArrivalTime(5));
    assertEquals(5 * 60 + 12, updatedTripTimesA.getDepartureTime(5));
  }

  @Test
  public void testPassedUpdate() {
    TripTimes updatedTripTimesA = createInitialTripTimes().copyOfScheduledTimes();

    updatedTripTimesA.updateDepartureTime(0, 30);

    assertEquals(30, updatedTripTimesA.getDepartureTime(0));
    assertEquals(60, updatedTripTimesA.getArrivalTime(1));
  }

  @Test
  public void testNegativeDwellTime() {
    TripTimes updatedTripTimesA = createInitialTripTimes().copyOfScheduledTimes();

    updatedTripTimesA.updateArrivalTime(1, 60);
    updatedTripTimesA.updateDepartureTime(1, 59);

    var error = updatedTripTimesA.validateNonIncreasingTimes();
    assertTrue(error.isPresent());
    assertEquals(1, error.get().stopIndex());
    assertEquals(NEGATIVE_DWELL_TIME, error.get().code());
  }

  @Test
  public void testNegativeHopTime() {
    TripTimes updatedTripTimesB = createInitialTripTimes().copyOfScheduledTimes();

    updatedTripTimesB.updateDepartureTime(6, 421);
    updatedTripTimesB.updateArrivalTime(7, 420);

    var error = updatedTripTimesB.validateNonIncreasingTimes();
    assertTrue(error.isPresent());
    assertEquals(7, error.get().stopIndex());
    assertEquals(NEGATIVE_HOP_TIME, error.get().code());
  }

  @Test
  public void testNonIncreasingUpdateCrossingMidnight() {
    TripTimes updatedTripTimesA = createInitialTripTimes().copyOfScheduledTimes();

    updatedTripTimesA.updateArrivalTime(0, -300); //"Yesterday"
    updatedTripTimesA.updateDepartureTime(0, 50);

    assertTrue(updatedTripTimesA.validateNonIncreasingTimes().isEmpty());
  }

  @Test
  public void testDelay() {
    TripTimes updatedTripTimesA = createInitialTripTimes().copyOfScheduledTimes();
    updatedTripTimesA.updateDepartureDelay(0, 10);
    updatedTripTimesA.updateArrivalDelay(6, 13);

    assertEquals(10, updatedTripTimesA.getDepartureTime(0));
    assertEquals(6 * 60 + 13, updatedTripTimesA.getArrivalTime(6));
  }

  @Test
  public void testCancel() {
    TripTimes updatedTripTimesA = createInitialTripTimes().copyOfScheduledTimes();
    updatedTripTimesA.cancelTrip();
    assertEquals(RealTimeState.CANCELED, updatedTripTimesA.getRealTimeState());
  }

  @Test
  public void testNoData() {
    TripTimes updatedTripTimesA = createInitialTripTimes().copyOfScheduledTimes();
    updatedTripTimesA.setNoData(1);
    assertFalse(updatedTripTimesA.isNoDataStop(0));
    assertTrue(updatedTripTimesA.isNoDataStop(1));
    assertFalse(updatedTripTimesA.isNoDataStop(2));
  }

  @Nested
  class GtfsStopSequence {

    @Test
    void gtfsSequence() {
      var stopIndex = createInitialTripTimes().gtfsSequenceOfStopIndex(2);
      assertEquals(20, stopIndex);
    }

    @Test
    void stopIndexOfGtfsSequence() {
      var stopIndex = createInitialTripTimes().stopIndexOfGtfsSequence(40);
      assertTrue(stopIndex.isPresent());
      assertEquals(4, stopIndex.getAsInt());
    }

    @Test
    void unknownGtfsSequence() {
      var stopIndex = createInitialTripTimes().stopIndexOfGtfsSequence(4);
      assertTrue(stopIndex.isEmpty());
    }
  }

  @Test
  public void validateNegativeDwellTime() {
    var tt = createInitialTripTimes();
    var updatedTt = tt.copyOfScheduledTimes();

    updatedTt.updateArrivalTime(2, 69);
    updatedTt.updateDepartureTime(2, 68);

    var validationResult = updatedTt.validateNonIncreasingTimes();
    assertTrue(validationResult.isPresent());
    assertEquals(2, validationResult.get().stopIndex());
    assertEquals(NEGATIVE_DWELL_TIME, validationResult.get().code());
  }

  @Test
  public void validateNegativeHopTime() {
    var tt = createInitialTripTimes();
    var updatedTt = tt.copyOfScheduledTimes();

    updatedTt.updateArrivalTime(2, 59);

    var validationResult = updatedTt.validateNonIncreasingTimes();
    assertTrue(validationResult.isPresent());
    assertEquals(2, validationResult.get().stopIndex());
    assertEquals(NEGATIVE_HOP_TIME, validationResult.get().code());
  }
}
