package org.opentripplanner.street.api.path;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.opentripplanner.street.api.request.StreetSearchRequest;
import org.opentripplanner.street.model.StreetMode;
import org.opentripplanner.street.search.state.TestStateBuilder;

class CompositeStreetPathTest {

  private static final Instant START_TIME = Instant.parse("2007-12-03T10:15:30.00Z");

  private static final StreetPath FIRST = new DefaultStreetPath(
    startState().streetEdge().streetEdge().build()
  );
  private static final StreetPath SECOND = FIRST.continueSearchAfter(
    Duration.ofSeconds(60),
    new DefaultStreetPath(startState().streetEdge().build())
  ).orElseThrow();

  private static final StreetPath SUBJECT = StreetPath.concat(FIRST, SECOND);

  @Test
  void times() {
    assertEquals(FIRST.startTime(), SUBJECT.startTime());
    assertEquals(SECOND.endTime(), SUBJECT.endTime());
    assertEquals(FIRST.duration().plusSeconds(60).plus(SECOND.duration()), SUBJECT.duration());
  }

  @Test
  void generalizedCost() {
    assertEquals(
      FIRST.generalizedCost() + SECOND.generalizedCost(),
      SUBJECT.generalizedCost(),
      1e-9
    );
  }

  @Test
  void traversalDistance() {
    assertEquals(
      FIRST.traversalDistance() + SECOND.traversalDistance(),
      SUBJECT.traversalDistance(),
      1e-9
    );
  }

  @Test
  void vertices() {
    assertEquals(FIRST.fromVertex(), SUBJECT.fromVertex());
    assertEquals(SECOND.toVertex(), SUBJECT.toVertex());
    assertFalse(SUBJECT.isEmpty());
  }

  @Test
  void legs() {
    var legs = SUBJECT.computeLegs(0);

    assertThat(legs).hasSize(2);
    assertEquals(FIRST.computeLegs(0).getFirst().startTime(), legs.get(0).startTime());
    assertEquals(SECOND.computeLegs(0).getFirst().startTime(), legs.get(1).startTime());
  }

  @Test
  void modes() {
    assertThat(SUBJECT.containsOnlyWalkMode()).isTrue();
    assertThat(SUBJECT.containsModeCar()).isFalse();
  }

  @Test
  void searchCannotBeContinued() {
    assertThrows(UnsupportedOperationException.class, () -> SUBJECT.continueSearch(FIRST));
  }

  private static TestStateBuilder startState() {
    var request = StreetSearchRequest.of()
      .withStartTime(START_TIME)
      .withMode(StreetMode.WALK)
      .build();
    return TestStateBuilder.of(request);
  }
}
