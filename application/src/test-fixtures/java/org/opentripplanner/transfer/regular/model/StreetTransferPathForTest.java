package org.opentripplanner.transfer.regular.model;

import org.opentripplanner.core.model.id.FeedScopedId;
import org.opentripplanner.street.api.path.StreetPath;

/**
 * Creates {@link StreetTransferPath}s for tests.
 */
public class StreetTransferPathForTest {

  private StreetTransferPathForTest() {}

  /**
   * A transfer without a street path, like a straight-line transfer.
   */
  public static StreetTransferPath straightLine(FeedScopedId stopId, double distance) {
    return new StreetTransferPath(stopId, distance, StreetPath.EMPTY);
  }
}
