package com.bedatadriven.jackson.datatype.jts;

/*
 * Original code at https://github.com/bedatadriven/jackson-datatype-jts Apache2 license
 *
 */

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

/** @author lainard on 28/06/16. */
public class D3DPointTest extends BaseJtsModuleTest<Point> {

  @Override
  protected Class<Point> getType() {
    return Point.class;
  }

  @Override
  protected String createGeometryAsGeoJson() {
    return "{\"type\":\"Point\",\"coordinates\":[1.2346,2.3457,200.0]}";
  }

  @Override
  protected Point createGeometry() {
    return gf.createPoint(new Coordinate(1.2346, 2.3457, 200.0));
  }
}
