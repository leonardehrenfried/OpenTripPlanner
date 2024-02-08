package org.opentripplanner.routing.api.request;

import static org.opentripplanner.routing.api.request.Feature.ACCESS;
import static org.opentripplanner.routing.api.request.Feature.CYCLING;
import static org.opentripplanner.routing.api.request.Feature.DRIVING;
import static org.opentripplanner.routing.api.request.Feature.EGRESS;
import static org.opentripplanner.routing.api.request.Feature.PARKING;
import static org.opentripplanner.routing.api.request.Feature.PICKUP;
import static org.opentripplanner.routing.api.request.Feature.RENTING;
import static org.opentripplanner.routing.api.request.Feature.SCOOTER;
import static org.opentripplanner.routing.api.request.Feature.TRANSFER;
import static org.opentripplanner.routing.api.request.Feature.WALKING;

import java.util.EnumSet;
import java.util.Set;
import org.opentripplanner.framework.doc.DocumentedEnum;

enum Feature {
  ACCESS,
  EGRESS,
  TRANSFER,
  WALKING,
  CYCLING,
  DRIVING,
  SCOOTER,
  RENTING,
  PARKING,
  PICKUP,
}

public enum StreetMode implements DocumentedEnum<StreetMode> {
  /**
   * No street mode is set. This option is used if we do not want street routing at all in this part
   * of the search.
   */
  NOT_SET(ACCESS, TRANSFER, EGRESS),
  /**
   * Walk only
   */
  WALK(ACCESS, TRANSFER, EGRESS, WALKING),
  /**
   * Bike only
   */
  BIKE(ACCESS, TRANSFER, EGRESS, CYCLING),
  /**
   * Bike to a bike parking area, then walk the rest of the way.
   * <p>
   * Direct mode and access mode only.
   */
  BIKE_TO_PARK(ACCESS, WALKING, CYCLING, PARKING),
  /**
   * Walk to a bike rental point, bike to a bike rental drop-off point, and walk the rest of the
   * way. This can include bike rental at fixed locations or free-floating services.
   */
  BIKE_RENTAL(ACCESS, EGRESS, WALKING, CYCLING, RENTING),
  /**
   * Walk to a scooter rental point, ride a scooter to a scooter rental drop-off point, and walk the
   * rest of the way. This can include scooter rental at fixed locations or free-floating services.
   */
  SCOOTER_RENTAL(ACCESS, EGRESS, WALKING, SCOOTER, RENTING),
  /**
   * Car only
   * <p>
   * Direct mode only.
   */
  CAR(ACCESS, DRIVING),
  /**
   * Start in the car, drive to a parking area, and walk the rest of the way.
   * <p>
   * Direct mode and access mode only.
   */
  CAR_TO_PARK(ACCESS, WALKING, DRIVING, PARKING),
  /**
   * Walk to a pickup point along the road, drive to a drop-off point along the road, and walk the
   * rest of the way. This can include various taxi-services or kiss & ride.
   */
  CAR_PICKUP(ACCESS, EGRESS, WALKING, DRIVING, PICKUP),
  /**
   * Walk to a car rental point, drive to a car rental drop-off point and walk the rest of the way.
   * This can include car rental at fixed locations or free-floating services.
   */
  CAR_RENTAL(ACCESS, EGRESS, WALKING, DRIVING, RENTING),

  /**
   * Using a car hailing app like Uber or Lyft to get to a train station or all the way to the destination.
   */
  CAR_HAILING(ACCESS, EGRESS, DRIVING, PICKUP),

  /**
   * Encompasses all types of on-demand and flexible transportation.
   */
  FLEXIBLE(ACCESS, EGRESS, WALKING);


  private final Set<Feature> features;

  StreetMode(Feature first, Feature... rest) {
    this.features = EnumSet.of(first, rest);
  }

  public boolean accessAllowed() {
    return features.contains(ACCESS);
  }

  public boolean transferAllowed() {
    return features.contains(TRANSFER);
  }

  public boolean egressAllowed() {
    return features.contains(EGRESS);
  }

  public boolean includesWalking() {
    return features.contains(WALKING);
  }

  public boolean includesBiking() {
    return features.contains(CYCLING);
  }

  public boolean includesDriving() {
    return features.contains(DRIVING);
  }

  public boolean includesScooter() {
    return features.contains(SCOOTER);
  }

  public boolean includesRenting() {
    return features.contains(RENTING);
  }

  public boolean includesParking() {
    return features.contains(PARKING);
  }

  public boolean includesPickup() {
    return features.contains(PICKUP);
  }

  @Override
  public String typeDescription() {
    return "Routing modes on streets, including walking, biking, driving, and car-sharing.";
  }

  private static String GBFS_PREREQ =
    """
    
    _Prerequisite:_ Vehicle or station locations need to be added to OTP from dynamic data feeds.
    See [Configuring GBFS](UpdaterConfig.md#gbfs-vehicle-rental-systems) on how to add one.
    """;

  @Override
  public String enumValueDescription() {
    return switch (this) {
      case NOT_SET -> "";
      case WALK -> "Walking some or all of the way of the route.";
      case BIKE -> "Cycling for the entirety of the route or taking a bicycle onto the public transport and cycling from the arrival station to the destination.";
      case BIKE_TO_PARK -> """
        Leaving the bicycle at the departure station and walking from the arrival station to the destination.
        This mode needs to be combined with at least one transit mode otherwise it behaves like an ordinary bicycle journey.
        
        _Prerequisite:_ Bicycle parking stations present in the OSM file and visible to OTP by enabling the property `staticBikeParkAndRide` during graph build.
        """;
      case BIKE_RENTAL -> """
        Taking a rented, shared-mobility bike for part or the entirety of the route.
        """ +
      GBFS_PREREQ;
      case SCOOTER_RENTAL -> """
        Walking to a scooter rental point, riding a scooter to a scooter rental drop-off point, and walking the rest of the way.
        This can include scooter rental at fixed locations or free-floating services.
        """ +
      GBFS_PREREQ;
      case CAR_RENTAL -> """
        Walk to a car rental point, drive to a car rental drop-off point and walk the rest of the way.
        This can include car rental at fixed locations or free-floating services.
        """ +
      GBFS_PREREQ;
      case CAR -> """
        Driving your own car the entirety of the route.
        This can be combined with transit, where will return routes with a [Kiss & Ride](https://en.wikipedia.org/wiki/Park_and_ride#Kiss_and_ride_/_kiss_and_fly) component.
        This means that the car is not parked in a permanent parking area but rather the passenger is dropped off (for example, at an airport) and the driver continues driving the car away from the drop off location.
        """;
      case CAR_TO_PARK -> """
        Driving a car to the park-and-ride facilities near a station and taking publictransport.
        This mode needs to be combined with at least one transit mode otherwise, it behaves like an ordinary car journey.
        _Prerequisite:_ Park-and-ride areas near the stations need to be present in the OSM input file.
        """;
      case CAR_PICKUP -> "Walking to a pickup point along the road, driving to a drop-off point along the road, and walking the rest of the way. <br/> This can include various taxi-services or kiss & ride.";
      case CAR_HAILING -> """
        Using a car hailing app like Uber or Lyft to get to a train station or all the way to the destination.
        
        See [the sandbox documentation](sandbox/RideHailing.md) on how to configure it.
        """;
      case FLEXIBLE -> "Encompasses all types of on-demand and flexible transportation for example GTFS Flex or NeTEx Flexible Stop Places.";
    };
  }
}
