package org.opentripplanner.routing.api.request.preference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.opentripplanner.routing.api.request.preference.ImmutablePreferencesAsserts.assertEqualsAndHashCode;

import org.junit.jupiter.api.Test;
import org.opentripplanner.routing.core.BicycleOptimizeType;

class BikePreferencesTest {

  public static final double SPEED = 2.0;
  public static final double RELUCTANCE = 1.2;
  public static final double WALKING_SPEED = 1.15;
  public static final int BOARD_COST = 660;
  public static final double WALKING_RELUCTANCE = 1.45;
  public static final int SWITCH_TIME = 200;
  public static final int SWITCH_COST = 450;
  public static final int PARK_TIME = 330;
  public static final int PARK_COST = 950;
  public static final TimeSlopeSafetyTriangle TRIANGLE = TimeSlopeSafetyTriangle
    .of()
    .withSlope(1)
    .build();
  public static final BicycleOptimizeType OPTIMIZE_TYPE = BicycleOptimizeType.TRIANGLE;

  private final BikePreferences subject = BikePreferences
    .of()
    .setSpeed(SPEED)
    .setReluctance(RELUCTANCE)
    .setBoardCost(BOARD_COST)
    .setWalkingSpeed(WALKING_SPEED)
    .setWalkingReluctance(WALKING_RELUCTANCE)
    .setSwitchTime(SWITCH_TIME)
    .setSwitchCost(SWITCH_COST)
    .setParkTime(PARK_TIME)
    .setParkCost(PARK_COST)
    .setOptimizeType(OPTIMIZE_TYPE)
    .withOptimizeTriangle(it -> it.withSlope(1).build())
    .build();

  @Test
  void speed() {
    assertEquals(SPEED, subject.speed());
  }

  @Test
  void reluctance() {
    assertEquals(RELUCTANCE, subject.reluctance());
  }

  @Test
  void boardCost() {
    assertEquals(BOARD_COST, subject.boardCost());
  }

  @Test
  void walkingSpeed() {
    assertEquals(WALKING_SPEED, subject.walkingSpeed());
  }

  @Test
  void walkingReluctance() {
    assertEquals(WALKING_RELUCTANCE, subject.walkingReluctance());
  }

  @Test
  void switchTime() {
    assertEquals(SWITCH_TIME, subject.switchTime());
  }

  @Test
  void switchCost() {
    assertEquals(SWITCH_COST, subject.switchCost());
  }

  @Test
  void parkTime() {
    assertEquals(PARK_TIME, subject.parkTime());
  }

  @Test
  void parkCost() {
    assertEquals(PARK_COST, subject.parkCost());
  }

  @Test
  void optimizeType() {
    assertEquals(OPTIMIZE_TYPE, subject.optimizeType());
  }

  @Test
  void optimizeTriangle() {
    assertEquals(TRIANGLE, subject.optimizeTriangle());
  }

  @Test
  void testOfAndCopyOf() {
    // Return same object if no value is set
    assertSame(BikePreferences.DEFAULT, BikePreferences.of().build());
    assertSame(subject, subject.copyOf().build());
  }

  @Test
  void testCopyOfEqualsAndHashCode() {
    // Create a copy, make a change and set it back again to force creating a new object
    var other = subject.copyOf().setSpeed(0.0).build();
    var same = other.copyOf().setSpeed(SPEED).build();
    assertEqualsAndHashCode(StreetPreferences.DEFAULT, subject, other, same);
  }

  @Test
  void testToString() {
    assertEquals("BikePreferences{}", BikePreferences.DEFAULT.toString());
    assertEquals(
      "BikePreferences{" +
      "speed: 2.0, " +
      "reluctance: 1.2, " +
      "boardCost: 660, " +
      "walkingSpeed: 1.15, " +
      "walkingReluctance: 1.45, " +
      "switchTime: 3m20s, " +
      "switchCost: 450, " +
      "parkTime: 5m30s, " +
      "parkCost: 950, " +
      "optimizeType: TRIANGLE, " +
      "optimizeTriangle: TimeSlopeSafetyTriangle[time=0.0, slope=1.0, safety=0.0]" +
      "}",
      subject.toString()
    );
  }
}
