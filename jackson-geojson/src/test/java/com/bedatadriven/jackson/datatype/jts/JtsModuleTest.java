package com.bedatadriven.jackson.datatype.jts;

/*
 * Original code at https://github.com/bedatadriven/jackson-datatype-jts Apache2 license
 *
 */

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;

public class JtsModuleTest {

  private ObjectMapper mapper;

  @BeforeEach
  public void setupMapper() {
    mapper = new ObjectMapper();
    mapper.registerModule(new JtsModule());
  }

  @Test
  public void invalidGeometryType() {
    String json = "{\"type\":\"Singularity\",\"coordinates\":[]}";
    assertThrows(JsonMappingException.class, () -> mapper.readValue(json, Geometry.class));
  }
}
