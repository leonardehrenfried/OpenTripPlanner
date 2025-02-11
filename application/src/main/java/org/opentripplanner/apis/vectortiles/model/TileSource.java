package org.opentripplanner.apis.vectortiles.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import org.opentripplanner.utils.lang.StringUtils;

/**
 * Represent a data source where Maplibre can fetch data for rendering directly in the browser.
 */
public sealed interface TileSource {
  @JsonSerialize
  String type();

  String id();

  String name();

  /**
   * Represents a vector tile source which is rendered into a map in the browser.
   */
  record VectorSource(String id, @JsonIgnore String url) implements TileSource {
    @Override
    public String type() {
      return "vector";
    }

    @Override
    public String name() {
      return id;
    }

    @JsonSerialize
    public int maxzoom() {
      return 25;
    }

    @JsonSerialize
    public int minzoom() {
      return 8;
    }

    @JsonSerialize
    public List<String> tiles() {
      return List.of(url);
    }
  }

  /**
   * Represents a raster-based source for map tiles. These are used mainly for background
   * map layers with vector data being rendered on top of it.
   */
  record RasterSource(
    String name,
    List<String> tiles,
    int maxzoom,
    int tileSize,
    String attribution
  )
    implements TileSource {
    @Override
    public String type() {
      return "raster";
    }

    public String id() {
      return StringUtils.slugify(name);
    }
  }
}
