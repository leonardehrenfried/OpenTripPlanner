package org.opentripplanner.graph_builder.issues;

import org.opentripplanner.graph_builder.issue.api.DataImportIssue;

public record MissingProjectionInServiceLink(String serviceLinkId) implements DataImportIssue {
  private static final String FMT =
    "Creating straight line path between Quays for ServiceLink with missing projection: %s";

  @Override
  public String getMessage() {
    return FMT.formatted(serviceLinkId);
  }
}
