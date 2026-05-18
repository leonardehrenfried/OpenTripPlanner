package com.bedatadriven.jackson.datatype.jts;

/*
 * Original code at https://github.com/bedatadriven/jackson-datatype-jts Apache2 license
 *
 */

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;

/** Created by mihaildoronin on 11/11/15. */
public class MultiPointTest extends BaseJtsModuleTest<MultiPoint> {

  @Override
  protected Class<MultiPoint> getType() {
    return MultiPoint.class;
  }

  @Override
  protected String createGeometryAsGeoJson() {
    return "{\"type\":\"MultiPoint\",\"coordinates\":[[1.2346,2.3457]]}";
  }

  @Override
  protected MultiPoint createGeometry() {
    return gf.createMultiPoint(
      new Point[] { gf.createPoint(new Coordinate(1.2345678, 2.3456789)) }
    );
  }
}
