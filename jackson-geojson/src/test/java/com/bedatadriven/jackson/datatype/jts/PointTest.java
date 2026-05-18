package com.bedatadriven.jackson.datatype.jts;

/*
 * Original code at https://github.com/bedatadriven/jackson-datatype-jts Apache2 license
 *
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.RoundingMode;
import java.util.stream.IntStream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/** Created by mihaildoronin on 11/11/15. */
class PointTest {

  private static final GeometryFactory GF = new GeometryFactory();

  static IntStream precisions() {
    return IntStream.of(1, 2, 3, 4, 5, 6, 7);
  }

  @ParameterizedTest
  @MethodSource("precisions")
  void shouldDeserializeConcreteType(int maxDecimals) throws Exception {
    var mapper = createMapper(maxDecimals);
    Point concreteGeometry = mapper.readValue(expectedJson(maxDecimals), Point.class);
    assertEquals(expectedJson(maxDecimals), mapper.writer().writeValueAsString(concreteGeometry));
  }

  @ParameterizedTest
  @MethodSource("precisions")
  void shouldDeserializeAsInterface(int maxDecimals) throws Exception {
    var mapper = createMapper(maxDecimals);
    Point geometry = GF.createPoint(new Coordinate(1.2345678, 2.3456789));
    String json = mapper.writer().writeValueAsString(geometry);
    assertEquals(expectedJson(maxDecimals), json);
    Geometry regeom = mapper.readerFor(Geometry.class).readValue(json);
    assertTrue(geometry.equalsExact(regeom, Math.pow(10, -maxDecimals)));
  }

  private static ObjectMapper createMapper(int maxDecimals) {
    var mapper = new ObjectMapper();
    mapper.registerModule(new JtsModule(GF, maxDecimals, 1, RoundingMode.HALF_UP));
    return mapper;
  }

  private static String expectedJson(int maxDecimals) {
    return switch (maxDecimals) {
      case 1 -> "{\"type\":\"Point\",\"coordinates\":[1.2,2.3]}";
      case 2 -> "{\"type\":\"Point\",\"coordinates\":[1.23,2.35]}";
      case 3 -> "{\"type\":\"Point\",\"coordinates\":[1.235,2.346]}";
      case 4 -> "{\"type\":\"Point\",\"coordinates\":[1.2346,2.3457]}";
      case 5 -> "{\"type\":\"Point\",\"coordinates\":[1.23457,2.34568]}";
      case 6 -> "{\"type\":\"Point\",\"coordinates\":[1.234568,2.345679]}";
      case 7 -> "{\"type\":\"Point\",\"coordinates\":[1.2345678,2.3456789]}";
      default -> throw new IllegalArgumentException("Unexpected precision: " + maxDecimals);
    };
  }
}
