package org.opentripplanner.model;

public enum WheelChairBoarding {
  NO_INFORMATION(0),
  POSSIBLE(1),
  NOT_POSSIBLE(2);

  public final int gtfsCode;

  WheelChairBoarding(int gtfsCode) {
    this.gtfsCode = gtfsCode;
  }

  public static WheelChairBoarding valueOfGtfsCode(int gtfsCode) {
    for (WheelChairBoarding value : values()) {
      if (value.gtfsCode == gtfsCode) {
        return value;
      }
    }
    throw new IllegalArgumentException("Unknown GTFS WheelChairBoardingType: " + gtfsCode);
  }
}
