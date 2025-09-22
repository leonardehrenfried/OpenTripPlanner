package org.opentripplanner.osm.issues;

import org.opentripplanner.graph_builder.issue.api.DataImportIssue;
import org.opentripplanner.osm.model.OsmEntity;

public record FloorNumberUnknownAssumedGroundLevel(String layer, OsmEntity entity)
  implements DataImportIssue {
  private static final String FMT =
    "%s : could not determine floor number for layer %s, assumed to be ground-level.";

  private static final String HTMLFMT =
    "<a href='%s'>'%s'</a> : could not determine floor number for layer %s, assumed to be ground-level.";

  @Override
  public String getMessage() {
    return FMT.formatted(entity.getId(), layer);
  }

  @Override
  public String getHTMLMessage() {
    return HTMLFMT.formatted(entity.url(), entity.getId(), layer);
  }
}
