package org.opentripplanner.common;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.locationtech.jts.geom.Geometry;
import org.opentripplanner.common.geometry.GeometryUtils;

public class GeojsonIoUrlGenerator {

  private static final ObjectMapper geoJsonMapper = new ObjectMapper()
    .registerModule(new JtsModule(GeometryUtils.getGeometryFactory()));

  public static String geometryUrl(Geometry g) {
    var x = geoJsonMapper.valueToTree(g).toString();
    var query = URLEncoder.encode(x, StandardCharsets.UTF_8);

    return "https://geojson.io/#data=data:application/json,%s".formatted(query);
  }
}
