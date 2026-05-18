package com.bedatadriven.jackson.datatype.jts;

/*
 * Original code at https://github.com/bedatadriven/jackson-datatype-jts Apache2 license
 *
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.math.RoundingMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

/** Created by mihaildoronin on 11/11/15. */
public abstract class BaseJtsModuleTest<T extends Geometry> {

  protected GeometryFactory gf = new GeometryFactory();
  private ObjectWriter writer;
  private ObjectMapper mapper;
  private T geometry;
  private String geometryAsGeoJson;

  protected BaseJtsModuleTest() {}

  @BeforeEach
  public void setup() {
    mapper = new ObjectMapper();
    mapper.registerModule(
      new JtsModule(new GeometryFactory(), getMaxDecimals(), getMinDecimals(), RoundingMode.HALF_UP)
    );
    writer = mapper.writer();
    geometry = createGeometry();
    geometryAsGeoJson = createGeometryAsGeoJson();
  }

  protected int getMaxDecimals() {
    return 4;
  }

  protected int getMinDecimals() {
    return 1;
  }

  protected abstract Class<T> getType();

  protected abstract String createGeometryAsGeoJson();

  protected abstract T createGeometry();

  @Test
  public void shouldDeserializeConcreteType() throws Exception {
    T concreteGeometry = mapper.readValue(geometryAsGeoJson, getType());
    assertEquals(geometryAsGeoJson, toJson(concreteGeometry));
  }

  @Test
  public void shouldDeserializeAsInterface() throws Exception {
    assertRoundTrip(geometry);
    assertEquals(geometryAsGeoJson, toJson(geometry));
  }

  protected String toJson(Object value) throws IOException {
    return writer.writeValueAsString(value);
  }

  protected void assertRoundTrip(T geom) throws IOException {
    String json = writer.writeValueAsString(geom);
    Geometry regeom = mapper.readerFor(Geometry.class).readValue(json);
    assertTrue(geom.equalsExact(regeom, Math.pow(10, -getMaxDecimals())));
  }
}
