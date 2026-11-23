package org.opentripplanner.street.model.path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.opentripplanner.street.model.StreetMode;
import org.opentripplanner.street.model.StreetModelFactory;
import org.opentripplanner.street.model.edge.TestEdge;
import org.opentripplanner.street.search.request.StreetSearchRequest;
import org.opentripplanner.street.search.state.State;
import org.opentripplanner.street.search.state.TestStateBuilder;

class DefaultStreetSearchResultTest {

  private static final Instant START_TIME = Instant.parse("2007-12-03T10:15:30.00Z");

  @ParameterizedTest
  @ValueSource(booleans = { false, true })
  void cheapAccessorsReadTheFinalState(boolean arriveBy) {
    var state = search(arriveBy);
    var subject = DefaultStreetSearchResult.of(state);

    assertEquals(arriveBy, subject.isArriveBy());
    assertEquals(state.getWeight(), subject.generalizedCost());
    assertEquals(Duration.ofSeconds(state.getElapsedTimeSeconds()), subject.duration());
    assertSame(state.getVertex(), subject.finalVertex());
    var path = new DefaultStreetPath(state);
    assertEquals(path.startTime(), subject.startTime());
    assertEquals(path.endTime(), subject.endTime());
  }

  @ParameterizedTest
  @ValueSource(booleans = { false, true })
  void toPathIsChronological(boolean arriveBy) {
    var state = search(arriveBy);
    var expected = new DefaultStreetPath(state);

    var path = (DefaultStreetPath) DefaultStreetSearchResult.of(state).toPath();

    assertEquals(expected.edges(), path.edges());
    assertEquals(expected.fromVertex(), path.fromVertex());
    assertEquals(expected.toVertex(), path.toVertex());
    assertEquals(expected.startTime(), path.startTime());
    assertEquals(expected.endTime(), path.endTime());
    assertEquals(expected.duration(), path.duration());
    assertEquals(expected.generalizedCost(), path.generalizedCost(), 0.001);
  }

  @Test
  void continueSearchAfter() {
    var result = DefaultStreetSearchResult.of(
      TestStateBuilder.of(request(false))
        .testEdge(b -> b.withDurationSeconds(10).withWeight(10))
        .build()
    );
    var other = new DefaultStreetPath(
      TestStateBuilder.of(request(false))
        .testEdge(b -> b.withDurationSeconds(5).withWeight(5))
        .testEdge(b -> b.withDurationSeconds(5).withWeight(5))
        .build()
    );

    var continued = result.continueSearchAfter(Duration.ofSeconds(60), other).orElseThrow();

    var path = continued.toPath();
    assertEquals(other.fromVertex(), path.fromVertex());
    assertEquals(other.toVertex(), path.toVertex());
    assertEquals(START_TIME.plusSeconds(70), continued.startTime());
    assertEquals(START_TIME.plusSeconds(80), continued.endTime());
    assertEquals(Duration.ofSeconds(10), continued.duration());
    assertEquals(10.0, continued.generalizedCost());
  }

  @ParameterizedTest
  @ValueSource(booleans = { false, true })
  void continueSearchAfterAlongASearchResult(boolean arriveBy) {
    var result = DefaultStreetSearchResult.of(search(arriveBy));
    var otherState = search(arriveBy);

    var alongResult = result.continueSearchAfter(
      Duration.ofSeconds(60),
      DefaultStreetSearchResult.of(otherState)
    );
    var alongPath = result.continueSearchAfter(
      Duration.ofSeconds(60),
      new DefaultStreetPath(otherState)
    );

    assertEquals(
      ((DefaultStreetPath) alongPath.orElseThrow().toPath()).edges(),
      ((DefaultStreetPath) alongResult.orElseThrow().toPath()).edges()
    );
  }

  @ParameterizedTest
  @ValueSource(booleans = { false, true })
  void withoutStatesIsChronological(boolean arriveBy) {
    var state = search(arriveBy);
    var expected = (DefaultStreetPath) DefaultStreetSearchResult.of(state).toPath();

    var path = (DefaultStreetPath) DefaultStreetSearchResult.of(state).withoutStates();

    assertEquals(expected.edges(), path.edges());
    assertEquals(expected.fromVertex(), path.fromVertex());
    assertEquals(expected.toVertex(), path.toVertex());
    assertEquals(expected.startTime(), path.startTime());
    assertEquals(expected.endTime(), path.endTime());
  }

  @Test
  void aPathIsItsOwnResult() {
    var path = DefaultStreetSearchResult.of(search(false)).toPath();
    assertSame(path, path.toPath());
  }

  /// A search over three edges. An arriveBy search traverses the edges backward in time, so each
  /// edge leads to the vertex the search is at.
  private static State search(boolean arriveBy) {
    var state = new State(StreetModelFactory.intersectionVertex(0, 0), request(arriveBy));
    int[][] steps = { { 10, 10 }, { 5, 5 }, { 10, 10 } };
    for (int i = 0; i < steps.length; i++) {
      var current = state.getVertex();
      var next = StreetModelFactory.intersectionVertex(i + 1, i + 1);
      var step = steps[i];
      var edge = (arriveBy ? TestEdge.of(next, current) : TestEdge.of(current, next))
        .withDurationSeconds(step[0])
        .withWeight(step[0])
        .withDistanceMeters(step[1])
        .build();
      state = edge.traverse(state)[0];
    }
    return state;
  }

  private static StreetSearchRequest request(boolean arriveBy) {
    return StreetSearchRequest.of()
      .withStartTime(START_TIME)
      .withMode(StreetMode.WALK)
      .withArriveBy(arriveBy)
      .build();
  }
}
