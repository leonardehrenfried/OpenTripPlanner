package org.opentripplanner.street.api.path;

import static org.opentripplanner.street.search.state.VehicleRentalState.RENTING_FLOATING;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.opentripplanner.service.vehiclerental.street.VehicleRentalEdge;
import org.opentripplanner.street.search.state.State;

/**
 * Splits a {@link StreetPath} into {@link StreetLeg}s. If the whole path is traversed with a
 * singular street mode, this will return a single leg. Each change of street mode within a path
 * generates a new leg. Note, walking a bike does not cause a new leg to be generated.
 */
class StreetPathToLegsMapper {

  private final double ellipsoidToGeoidDifference;

  StreetPathToLegsMapper(double ellipsoidToGeoidDifference) {
    this.ellipsoidToGeoidDifference = ellipsoidToGeoidDifference;
  }

  private static boolean isRentalPickUp(State state) {
    return (
      state.getBackEdge() instanceof VehicleRentalEdge &&
      (state.getBackState() == null || !state.getBackState().isRentingVehicle())
    );
  }

  private static boolean isRentalStationDropOff(State state) {
    return (
      state.getBackEdge() instanceof VehicleRentalEdge && state.getBackState().isRentingVehicle()
    );
  }

  /**
   * Dropping of a free-floating vehicle can happen at any edge so be sure to select the correct
   * state (forward, not backward).
   */
  private static boolean isFloatingRentalDropoff(State state) {
    return (
      !state.isRentingVehicle() &&
      state.getBackState() != null &&
      state.getBackState().getVehicleRentalState() == RENTING_FLOATING
    );
  }

  /**
   * The legs are returned in the order they are traversed in the path.
   */
  List<StreetLeg> map(DefaultStreetPath path) {
    List<StreetLeg> legs = new ArrayList<>();
    DefaultStreetLeg previousStreetLeg = null;
    for (var slice : slicePath(path)) {
      var leg = new DefaultStreetLeg(
        slice.path(),
        slice.startTime(),
        slice.extraWeight(),
        previousStreetLeg,
        ellipsoidToGeoidDifference
      );
      legs.add(leg);
      previousStreetLeg = leg;
    }
    return List.copyOf(legs);
  }

  /**
   * Slice a street path at the leg boundaries.
   *
   * @param streetPath The path to slice of input states
   * @return A list of slices representing the final legs
   */
  private static List<Slice> slicePath(DefaultStreetPath streetPath) {
    var states = streetPath.states();
    // Trivial case
    if (states.stream().allMatch(state -> state.getBackMode() == null)) {
      return List.of();
    }

    List<Slice> slices = new ArrayList<>();

    int previousBreak = 0;
    Instant startTime = states.getFirst().getTime();
    double extraWeight = 0;

    for (int i = 1; i < states.size() - 1; i++) {
      var backState = states.get(i);
      var forwardState = states.get(i + 1);

      var rentalChange =
        isRentalPickUp(backState) ||
        isRentalStationDropOff(backState) ||
        isFloatingRentalDropoff(backState);
      var parkingChange = backState.isVehicleParked() != forwardState.isVehicleParked();
      var carPickupChange = backState.getCarPickupState() != forwardState.getCarPickupState();

      if (parkingChange || rentalChange || carPickupChange) {
        int nextBreak = i;

        if (nextBreak > previousBreak) {
          var subPath = streetPath.subPath(previousBreak, nextBreak + 1);
          slices.add(new Slice(subPath, startTime, extraWeight));
          // The next leg starts at the last state of this one
          startTime = backState.getTime();
          extraWeight = 0;
        }

        /* Remove the state for actually parking (traversing a VehicleParkingEdge) from the
         * states so that the leg from/to edges correspond to the actual entrances.
         * The start time stays the one before parking and the cost for parking is added as
         * extra weight, so that the time and cost for parking are added to the next leg.
         */
        if (parkingChange) {
          extraWeight += forwardState.getWeight() - backState.getWeight();
          nextBreak++;
        }

        previousBreak = nextBreak;
      }
    }

    // Final leg
    if (states.size() > previousBreak) {
      slices.add(
        new Slice(streetPath.subPath(previousBreak, states.size()), startTime, extraWeight)
      );
    }

    return slices;
  }

  /**
   * A sub-path of a street path that becomes a leg. If the leg starts by leaving a vehicle
   * parking, the parking state is not part of the sub-path. Then the start time is the time
   * before parking and the extra weight is the cost for parking. Otherwise, the start time is the
   * time of the first state of the sub-path and the extra weight is zero.
   */
  private record Slice(DefaultStreetPath path, Instant startTime, double extraWeight) {}
}
