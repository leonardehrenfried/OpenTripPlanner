package org.opentripplanner.street.model.path;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;
import org.opentripplanner.street.search.TraverseMode;
import org.opentripplanner.street.search.state.TestStateBuilder;

class StreetPathToLegsMapperTest {

  @Test
  void singleLeg() {
    var state = TestStateBuilder.ofWalking().streetEdge().streetEdge().build();

    var legs = StreetPathForTest.path(state).computeLegs(0);

    assertThat(legs).hasSize(1);
    var leg = legs.getFirst();
    assertThat(leg.computeMode()).isEqualTo(TraverseMode.WALK);
    assertThat(leg.computeSteps()).hasSize(2);
    assertThat(leg.distanceMeters()).isEqualTo(200);
    assertThat(leg.rentedVehicle()).isFalse();
  }

  @Test
  void carRentalPickUp() {
    var state = TestStateBuilder.ofCarRental().streetEdge().pickUpCarFromStation().build();

    var legs = StreetPathForTest.path(state).computeLegs(0);

    assertThat(legs).hasSize(2);
    var walk = legs.get(0);
    var car = legs.get(1);
    assertThat(walk.computeMode()).isEqualTo(TraverseMode.WALK);
    assertThat(car.computeMode()).isEqualTo(TraverseMode.CAR);
    assertThat(car.rentedVehicle()).isTrue();
    assertThat(walk.endTime()).isEqualTo(car.startTime());
  }

  @Test
  void noLegsForEmptyPath() {
    var state = TestStateBuilder.ofWalking().build();

    assertThat(StreetPathForTest.path(state).computeLegs(0)).isEmpty();
  }
}
