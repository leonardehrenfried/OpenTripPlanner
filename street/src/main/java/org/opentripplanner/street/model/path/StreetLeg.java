package org.opentripplanner.street.model.path;

import java.util.List;
import javax.annotation.Nullable;
import org.locationtech.jts.geom.LineString;
import org.opentripplanner.street.model.elevation.ElevationProfile;
import org.opentripplanner.street.model.path.step.StreetStep;
import org.opentripplanner.street.search.TraverseMode;

/**
 * A part of a {@link StreetPath} which is traversed in a single street mode. Note, walking a bike
 * does not start a new leg.
 */
public non-sealed interface StreetLeg extends PathLeg {
  /**
   * The mode of the leg. If a vehicle is rented, this is the mode of the rented vehicle.
   */
  TraverseMode mode();

  StreetLegPlace from();

  StreetLegPlace to();

  double distanceMeters();

  int generalizedCost();

  LineString geometry();

  /**
   * The elevation profile of the leg, or null if no elevation data is available.
   */
  @Nullable
  ElevationProfile elevationProfile();

  /**
   * The total elevation gained and lost along the leg.
   */
  ElevationChange elevationChange();

  /**
   * The turn-by-turn directions of this leg.
   */
  List<StreetStep> steps();

  /**
   * Whether the leg is traversed with a rented vehicle.
   */
  boolean rentedVehicle();

  /**
   * Whether the leg ends while still renting a vehicle picked up at a rental station.
   */
  boolean arrivesWithRentedVehicleFromStation();

  /**
   * The network of the rented vehicle, or null if no vehicle is rented or the network is unknown.
   */
  @Nullable
  String vehicleRentalNetwork();
}
