package org.opentripplanner.graph_builder.issue.report;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Collection;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.PropertyType;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.locationtech.jts.geom.Geometry;

/**
 * Wrapper to handle writing GeoJSON FeatureCollections
 *
 */
class FeatureCollectionWriter implements AutoCloseable {

  // Set slightly higher resolution than default. We output small details
  private static final int MAX_DECIMALS = 6;

  private final OutputStream out;

  private final JsonGenerator generator;

  private final ObjectMapper mapper;

  private boolean inArray = false;

  /**
   * Prepares a writer over the target output stream.
   *
   * @param outputStream
   * @throws IOException
   */
  public FeatureCollectionWriter(OutputStream outputStream) throws IOException {
    // force the output CRS to be long, lat as required by spec
    mapper = new ObjectMapper();
    mapper.registerModule(new JtsModule(MAX_DECIMALS));

    if (outputStream instanceof BufferedOutputStream) {
      this.out = outputStream;
    } else {
      this.out = new BufferedOutputStream(outputStream);
    }
    JsonFactory factory = new JsonFactory();
    generator = factory.createGenerator(out);
    generator.setPrettyPrinter(new DefaultPrettyPrinter());

    generator.writeStartObject();
    generator.writeStringField("type", "FeatureCollection");
    generator.writeFieldName("features");
    generator.writeStartArray();
    inArray = true;
  }

  public void writeFeatureCollection(SimpleFeatureCollection features) throws IOException {
    try (SimpleFeatureIterator itr = features.features()) {
      while (itr.hasNext()) {
        SimpleFeature feature = itr.next();
        write(feature);
      }
    }
  }

  @Override
  public void close() throws IOException {
    try {
      if (inArray) {
        generator.writeEndArray();
        generator.writeEndObject();
      }

      generator.close();
    } finally {
      out.close();
    }
  }

  /**
   * @param currentFeature
   * @throws IOException
   * @throws JsonProcessingException
   */
  private void writeFeature(SimpleFeature currentFeature, JsonGenerator g)
    throws IOException, JsonProcessingException {
    Geometry defaultGeometry = (Geometry) currentFeature.getDefaultGeometry();
    g.writeStartObject();
    g.writeStringField("type", "Feature");

    g.writeFieldName("properties");
    g.writeStartObject();
    for (Property p : currentFeature.getProperties()) {
      PropertyType type = p.getType();
      if (type instanceof GeometryType) {
        continue;
      }
      Object value = p.getValue();
      String name = p.getName().getLocalPart();
      if (value == null) {
        g.writeNullField(name);
        continue;
      }
      Class<?> binding = p.getType().getBinding();
      g.writeFieldName(name);
      writeValue(g, value, binding);
    }
    g.writeEndObject();

    // Check CRS and Axis order before writing out to comply with
    // https://tools.ietf.org/html/rfc7946 unless they asked nicely
    if (defaultGeometry != null) {
      g.writeFieldName("geometry");
      String gString = mapper.writeValueAsString(defaultGeometry);
      g.writeRawValue(gString);
    } else {
      g.writeFieldName("geometry");
      g.writeNull();
    }
    g.writeStringField("id", currentFeature.getID());
    g.writeEndObject();
    g.flush();
  }

  /**
   * Writes a single feature onto the output.
   *
   * @param currentFeature
   * @throws IOException
   */
  private void write(SimpleFeature currentFeature) throws IOException {
    writeFeature(currentFeature, generator);
  }

  private void writeValue(JsonGenerator g, Object value, Class<?> binding) throws IOException {
    if (value == null) {
      g.writeNull();
      return;
    }

    if (binding == Integer.class) {
      g.writeNumber((int) value);
    } else if (binding == Double.class) {
      g.writeNumber((double) value);
    } else if (binding == Boolean.class) {
      g.writeBoolean((boolean) value);
    } else if (binding.isArray()) {
      g.writeStartArray();
      int length = Array.getLength(value);
      for (int i = 0; i < length; i++) {
        writeValue(g, Array.get(value, i), binding.getComponentType());
      }
      g.writeEndArray();
    } else if (Collection.class.isAssignableFrom(binding)) {
      g.writeStartArray();
      for (Object v : (Collection) value) {
        writeValue(g, v, v == null ? null : v.getClass());
      }
      g.writeEndArray();
    } else {
      g.writeString(value.toString());
    }
  }
  /*  Encodes the whole feature collection onto the output */
}
