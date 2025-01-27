package org.opentripplanner.transit.model.site;

import java.util.Objects;
import org.opentripplanner.framework.i18n.I18NString;
import org.opentripplanner.transit.model.framework.FeedScopedId;

/**
 * A place where a station connects to the street network. Equivalent to GTFS stop location .
 */
public final class Stairs {

  private final I18NString name;

  public Stairs(I18NString name) {
    this.name = name;
  }

}
