package org.opentripplanner.ext.flex.path;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.opentripplanner.ext.flex.FlexParameters;
import org.opentripplanner.ext.flex.flexpathcalculator.FlexPath;
import org.opentripplanner.model.plan.PlanTestConstants;
import org.opentripplanner.street.geometry.GeometryUtils;
import org.opentripplanner.street.model.StreetModelForTest;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.search.TraverseMode;

class FlexStreetPathTest implements PlanTestConstants {

  private static final FlexParameters FLEX_PARAMETERS = new FlexParameters() {
    @Override
    public Duration maxTransferDuration() {
      return FlexParameters.defaultValues().maxTransferDuration();
    }

    @Override
    public Duration maxFlexTripDuration() {
      return FlexParameters.defaultValues().maxFlexTripDuration();
    }

    @Override
    public Duration maxAccessWalkDuration() {
      return FlexParameters.defaultValues().maxAccessWalkDuration();
    }

    @Override
    public Duration maxEgressWalkDuration() {
      return FlexParameters.defaultValues().maxEgressWalkDuration();
    }

    @Override
    public int boardCost() {
      return 999;
    }

    @Override
    public double reluctance() {
      return 5.0;
    }
  };

  private static final Vertex FROM = StreetModelForTest.intersectionVertex(1, 1);
  private static final Vertex TO = StreetModelForTest.intersectionVertex(2, 2);
  private static final Instant START_TIME = Instant.parse("2025-01-15T12:00:00.400Z");

  @Test
  void path() {
    var subject = flexStreetPath(false);

    assertEquals(Duration.ofSeconds(600), subject.duration());
    assertEquals(3999.0, subject.generalizedCost(), 1e-9);
    assertEquals(START_TIME, subject.startTime());
    assertEquals(START_TIME.plusSeconds(600), subject.endTime());
    assertEquals(FROM, subject.fromVertex());
    assertEquals(TO, subject.toVertex());
    assertEquals(1000, subject.traversalDistance());
  }

  @Test
  void leg() {
    var subject = flexStreetPath(false);

    var legs = subject.computeLegs(0);

    assertThat(legs).hasSize(1);
    var leg = (FlexStreetLeg) legs.getFirst();
    assertThat(leg.path()).isSameInstanceAs(subject);
    assertThat(leg.computeMode()).isEqualTo(TraverseMode.FLEX);
    assertThat(leg.from().vertex()).isEqualTo(FROM);
    assertThat(leg.to().vertex()).isEqualTo(TO);
    assertThat(leg.generalizedCost()).isEqualTo(3999);
    assertThat(leg.distanceMeters()).isEqualTo(1000);
    assertThat(leg.geometry()).isEqualTo(subject.geometry());
    assertThat(leg.elevationProfile()).isNull();
    assertThat(leg.computeSteps()).isEmpty();
    assertThat(leg.rentedVehicle()).isFalse();
    assertThat(leg.vehicleRentalNetwork()).isNull();
  }

  @Test
  void legTimesAreRoundedUpForDepartAfterSearches() {
    var leg = flexStreetPath(false).computeLegs(0).getFirst();

    assertEquals(Instant.parse("2025-01-15T12:00:01Z"), leg.startTime());
    assertEquals(Instant.parse("2025-01-15T12:10:01Z"), leg.endTime());
  }

  @Test
  void legTimesAreRoundedDownForArriveBySearches() {
    var leg = flexStreetPath(true).computeLegs(0).getFirst();

    assertEquals(Instant.parse("2025-01-15T12:00:00Z"), leg.startTime());
    assertEquals(Instant.parse("2025-01-15T12:10:00Z"), leg.endTime());
  }

  private static FlexStreetPath flexStreetPath(boolean arriveBy) {
    return new FlexStreetPath(
      FROM,
      TO,
      A.stop.getId(),
      B.stop.getId(),
      null,
      1,
      2,
      LocalDate.of(2025, 1, 15),
      new FlexPath(1000, 600, () -> GeometryUtils.makeLineString(1, 1, 2, 2)),
      FLEX_PARAMETERS,
      START_TIME,
      arriveBy
    );
  }
}
