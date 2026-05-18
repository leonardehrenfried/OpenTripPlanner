/*
 * Original code at https://github.com/bedatadriven/jackson-datatype-jts Apache2 license
 *
 */

package com.bedatadriven.jackson.datatype.jts.parsers;

import org.locationtech.jts.geom.GeometryFactory;

/** Created by mihaildoronin on 11/11/15. */
public class BaseParser {

  protected final GeometryFactory geometryFactory;

  public BaseParser(GeometryFactory geometryFactory) {
    this.geometryFactory = geometryFactory;
  }
}
