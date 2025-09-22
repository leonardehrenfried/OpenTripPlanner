package org.opentripplanner.graph_builder.issues;

import org.opentripplanner.graph_builder.issue.api.DataImportIssue;

public record TurnRestrictionBad(long id, String reason) implements DataImportIssue {
  private static final String FMT = "Bad turn restriction at relation %s. Reason: %s";
  private static final String HTMLFMT =
    "Bad turn restriction at relation <a href='http://www.openstreetmap.org/relation/%s'>%s</a>. Reason: %s";

  @Override
  public String getMessage() {
    return FMT.formatted(id, reason);
  }

  @Override
  public String getHTMLMessage() {
    return HTMLFMT.formatted(id, id, reason);
  }
}
