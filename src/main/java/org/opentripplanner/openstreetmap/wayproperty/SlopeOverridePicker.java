package org.opentripplanner.openstreetmap.wayproperty;

import org.opentripplanner.openstreetmap.wayproperty.specifier.OsmSpecifier;

public class SlopeOverridePicker {

  private OsmSpecifier specifier;

  private boolean override;

  public SlopeOverridePicker() {}

  public SlopeOverridePicker(OsmSpecifier specifier, boolean override) {
    this.specifier = specifier;
    this.override = override;
  }

  public OsmSpecifier getSpecifier() {
    return specifier;
  }

  public boolean getOverride() {
    return override;
  }
}
