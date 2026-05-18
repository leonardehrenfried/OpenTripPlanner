/*
 * Original code at https://github.com/bedatadriven/jackson-datatype-jts Apache2 license
 *
 */
package com.bedatadriven.jackson.datatype.jts.serialization;

import com.bedatadriven.jackson.datatype.jts.parsers.GeometryParser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.locationtech.jts.geom.Geometry;

/** Created by mihaildoronin on 11/11/15. */
public class GeometryDeserializer<T extends Geometry> extends JsonDeserializer<T> {

  private GeometryParser<T> geometryParser;

  public GeometryDeserializer(GeometryParser<T> geometryParser) {
    this.geometryParser = geometryParser;
  }

  @Override
  public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
    throws IOException {
    ObjectCodec oc = jsonParser.getCodec();
    JsonNode root = oc.readTree(jsonParser);
    return geometryParser.geometryFromJson(root);
  }
}
