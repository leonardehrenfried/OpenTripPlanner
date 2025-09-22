package org.opentripplanner.graph_builder.issues;

import org.opentripplanner.graph_builder.issue.api.DataImportIssue;
import org.opentripplanner.osm.model.OsmEntity;

public record TurnRestrictionUnknown(OsmEntity entity, String tagval) implements DataImportIssue {
  private static final String FMT = "Invalid turn restriction tag %s in turn restriction %d";
  private static final String HTMLFMT = "Invalid turn restriction tag %s in <a href='%s'>'%s'</a>";

  @Override
  public String getMessage() {
    return FMT.formatted(tagval, entity.getId());
  }

  @Override
  public String getHTMLMessage() {
    return HTMLFMT.formatted(tagval, entity.url(), entity.getId());
  }
}
