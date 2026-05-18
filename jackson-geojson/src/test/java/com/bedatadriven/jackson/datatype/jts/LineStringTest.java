package com.bedatadriven.jackson.datatype.jts;

/*
 * Original code at https://github.com/bedatadriven/jackson-datatype-jts Apache2 license
 *
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

/** Created by mihaildoronin on 11/11/15. */
class LineStringTest {

  private static final GeometryFactory GF = new GeometryFactory();
  private static final String EXPECTED_JSON =
    "{\"type\":\"LineString\",\"coordinates\":[[100.0,0.0],[101.0,1.0]]}";

  static Stream<Locale> locales() {
    return Stream.of(Locale.ENGLISH, Locale.ITALIAN);
  }

  @ParameterizedTest
  @MethodSource("locales")
  void shouldDeserializeConcreteType(Locale locale) throws Exception {
    Locale previous = Locale.getDefault();
    Locale.setDefault(locale);
    try {
      ObjectMapper mapper = createMapper();
      LineString geom = mapper.readValue(EXPECTED_JSON, LineString.class);
      assertEquals(EXPECTED_JSON, mapper.writer().writeValueAsString(geom));
    } finally {
      Locale.setDefault(previous);
    }
  }

  @ParameterizedTest
  @MethodSource("locales")
  void shouldDeserializeAsInterface(Locale locale) throws Exception {
    Locale previous = Locale.getDefault();
    Locale.setDefault(locale);
    try {
      ObjectMapper mapper = createMapper();
      LineString geometry = GF.createLineString(
        new Coordinate[] { new Coordinate(100.0, 0.0), new Coordinate(101.0, 1.0) }
      );
      String json = mapper.writer().writeValueAsString(geometry);
      assertEquals(EXPECTED_JSON, json);
      Geometry regeom = mapper.readerFor(Geometry.class).readValue(json);
      assertTrue(geometry.equalsExact(regeom, Math.pow(10, -4)));
    } finally {
      Locale.setDefault(previous);
    }
  }

  private static ObjectMapper createMapper() {
    var mapper = new ObjectMapper();
    mapper.registerModule(new JtsModule(GF, 4, 1, RoundingMode.HALF_UP));
    return mapper;
  }
}
