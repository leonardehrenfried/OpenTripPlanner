package org.opentripplanner.graph_builder.issues;

import org.opentripplanner.graph_builder.issue.api.DataImportIssue;
import org.opentripplanner.transit.model.framework.FeedScopedId;

public record ShapeGeometryTooFar(FeedScopedId tripId, FeedScopedId shapeId)
  implements DataImportIssue {
  private static final String FMT =
    "Trip %s is too far from shape geometry %s, using straight line path instead";

  @Override
  public String getMessage() {
    return FMT.formatted(tripId, shapeId);
  }
}
