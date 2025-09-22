package org.opentripplanner.graph_builder.issues;

import org.opentripplanner.graph_builder.issue.api.DataImportIssue;
import org.opentripplanner.osm.model.OsmEntity;

public record LevelAmbiguous(String layerName, OsmEntity entity) implements DataImportIssue {
  private static final String FMT =
    "Could not infer floor number for layer called '%s' at %s. " +
    "Vertical movement will still be possible, but elevator cost might be incorrect. " +
    "Consider an OSM level map.";
  private static final String HTMLFMT =
    "Could not infer floor number for layer called <a href='%s'>'%s' (%d)</a>" +
    "Vertical movement will still be possible, but elevator cost might be incorrect. " +
    "Consider an OSM level map.";

  @Override
  public String getMessage() {
    return FMT.formatted(layerName, entity.getId());
  }

  @Override
  public String getHTMLMessage() {
    return HTMLFMT.formatted(entity.url(), layerName, entity.getId());
  }
}
