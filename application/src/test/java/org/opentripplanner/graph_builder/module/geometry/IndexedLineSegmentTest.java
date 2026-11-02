package org.opentripplanner.graph_builder.module.geometry;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.locationtech.jts.geom.Coordinate;
import org.opentripplanner.street.geometry.SphericalDistanceLibrary;

/**
 * Pins the exact output of {@link IndexedLineSegment}: the snapping of stops to shapes depends on
 * these values, so any change to them changes the generated hop geometries.
 */
class IndexedLineSegmentTest {

  private static final Coordinate START = new Coordinate(10.0, 50.0);
  private static final IndexedLineSegment EAST = new IndexedLineSegment(
    0,
    START,
    new Coordinate(10.01, 50.0)
  );
  private static final IndexedLineSegment NORTH_EAST = new IndexedLineSegment(
    0,
    START,
    new Coordinate(10.004, 50.003)
  );
  private static final IndexedLineSegment DEGENERATE = new IndexedLineSegment(0, START, START);

  private static final Coordinate BESIDE_MIDDLE = new Coordinate(10.005, 50.001);
  private static final Coordinate BEFORE_START = new Coordinate(9.995, 50.0005);
  private static final Coordinate AFTER_END = new Coordinate(10.015, 49.999);
  private static final Coordinate ON_EAST = new Coordinate(10.003, 50.0);
  private static final Coordinate ON_NORTH_EAST = new Coordinate(10.002, 50.0015);

  private static Stream<Arguments> cases() {
    return Stream.of(
      Arguments.of(EAST, BESIDE_MIDDLE, 111.12333943540068, 0.4999999997385039),
      Arguments.of(EAST, BEFORE_START, 361.4766172118811, 0.49998959723821773),
      Arguments.of(EAST, AFTER_END, 374.07567791572325, 1.5000207995400585),
      Arguments.of(EAST, ON_EAST, 0.010028594590370705, 0.2999999915761072),
      Arguments.of(EAST, ON_NORTH_EAST, 166.69527636463434, 0.2000093741693447),
      Arguments.of(EAST, START, 0.0, 0.0),
      Arguments.of(NORTH_EAST, BESIDE_MIDDLE, 198.88371418634893, 0.7215202955690503),
      Arguments.of(NORTH_EAST, BEFORE_START, 361.4766172118811, 0.43320857519503864),
      Arguments.of(NORTH_EAST, AFTER_END, 902.8139530615948, 1.3960149867823983),
      Arguments.of(NORTH_EAST, ON_EAST, 162.72616556560945, 0.31760342129658864),
      Arguments.of(NORTH_EAST, ON_NORTH_EAST, 0.004627798622728164, 0.5000032973729737),
      Arguments.of(NORTH_EAST, START, 0.0, 0.0),
      Arguments.of(DEGENERATE, BESIDE_MIDDLE, 374.0685839780584, Double.POSITIVE_INFINITY),
      Arguments.of(DEGENERATE, BEFORE_START, 361.4766172118811, Double.POSITIVE_INFINITY),
      Arguments.of(DEGENERATE, AFTER_END, 1077.3045598925232, Double.POSITIVE_INFINITY),
      Arguments.of(DEGENERATE, ON_EAST, 214.30913946015593, Double.NaN),
      Arguments.of(DEGENERATE, ON_NORTH_EAST, 219.54920001011038, Double.POSITIVE_INFINITY),
      Arguments.of(DEGENERATE, START, 0.0, 1.0)
    );
  }

  @ParameterizedTest
  @MethodSource("cases")
  void distanceAndFraction(
    IndexedLineSegment segment,
    Coordinate coord,
    double expectedDistance,
    double expectedFraction
  ) {
    assertEquals(expectedDistance, segment.distance(coord), 0.0);
    assertEquals(expectedFraction, segment.fraction(coord), 0.0);
  }

  @Test
  void isCertainlyFartherThanNorthAndSouth() {
    var north = new Coordinate(10.005, 50.009);
    var south = new Coordinate(10.005, 49.991);
    // both are about 1000 m from the segment
    assertTrue(EAST.isCertainlyFartherThan(north, 150));
    assertTrue(EAST.isCertainlyFartherThan(south, 150));
    // the safety margin makes the check conservative
    assertFalse(EAST.isCertainlyFartherThan(north, 1000));
    assertFalse(EAST.isCertainlyFartherThan(south, 1000));
  }

  @Test
  void isCertainlyFartherThanEastAndWest() {
    // at 50 degrees latitude, 0.014 degrees longitude is about 1000 m
    var east = new Coordinate(10.024, 50.0);
    var west = new Coordinate(9.986, 50.0);
    assertTrue(EAST.isCertainlyFartherThan(east, 150));
    assertTrue(EAST.isCertainlyFartherThan(west, 150));
    assertFalse(EAST.isCertainlyFartherThan(east, 1000));
    assertFalse(EAST.isCertainlyFartherThan(west, 1000));
  }

  @Test
  void isNotCertainlyFartherThanWhenClose() {
    assertFalse(EAST.isCertainlyFartherThan(BESIDE_MIDDLE, 150));
    assertFalse(EAST.isCertainlyFartherThan(ON_EAST, 0));
    assertFalse(DEGENERATE.isCertainlyFartherThan(START, 0));
  }

  @Test
  void isNeverCertainlyFartherThanForLongSegments() {
    var longSegment = new IndexedLineSegment(0, START, new Coordinate(10.2, 50.0));
    assertFalse(longSegment.isCertainlyFartherThan(new Coordinate(10.1, 51.0), 150));
  }

  @Test
  void isNeverCertainlyFartherThanNearTheAntimeridian() {
    var segment = new IndexedLineSegment(
      0,
      new Coordinate(179.999, -17.0),
      new Coordinate(179.9995, -17.0)
    );
    // close across the antimeridian, but far apart in longitude degrees
    var acrossAntimeridian = new Coordinate(-179.9995, -17.0);
    assertThat(segment.distance(acrossAntimeridian)).isLessThan(150);
    assertFalse(segment.isCertainlyFartherThan(acrossAntimeridian, 150));
  }

  /**
   * The check must never claim a segment is farther away than the distance computed by {@link
   * IndexedLineSegment#distance}: that would change which segments stops are snapped to.
   */
  @Test
  void isCertainlyFartherThanNeverContradictsDistance() {
    var meters = new double[] { 0, 10, 50, 150, 500, 2000 };
    int checked = 0;
    for (double lat : new double[] { 0, 50, 70, -45 }) {
      for (double bearing = 0; bearing < 360; bearing += 30) {
        var start = new Coordinate(10.0, lat);
        var end = move(start, bearing, 500);
        var segment = new IndexedLineSegment(0, start, end);
        for (double dLat = -0.03; dLat <= 0.03; dLat += 0.0005) {
          for (double dLon = -0.06; dLon <= 0.06; dLon += 0.001) {
            var coord = new Coordinate(start.x + dLon, start.y + dLat);
            double distance = segment.distance(coord);
            for (double m : meters) {
              if (segment.isCertainlyFartherThan(coord, m)) {
                assertThat(distance).isGreaterThan(m);
                checked++;
              }
            }
          }
        }
      }
    }
    // make sure the check actually skips most of the far away coordinates
    assertThat(checked).isGreaterThan(1_000_000);
  }

  private static Coordinate move(Coordinate from, double bearingDegrees, double meters) {
    double dLat = Math.toDegrees(
      (meters * Math.cos(Math.toRadians(bearingDegrees))) /
        SphericalDistanceLibrary.RADIUS_OF_EARTH_IN_M
    );
    double dLon =
      Math.toDegrees(
        (meters * Math.sin(Math.toRadians(bearingDegrees))) /
          SphericalDistanceLibrary.RADIUS_OF_EARTH_IN_M
      ) / Math.cos(Math.toRadians(from.y));
    return new Coordinate(from.x + dLon, from.y + dLat);
  }
}
