package org.opentripplanner.street.model.path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

  @ParameterizedTest
  @ValueSource(booleans = { false, true })
  void storedPathHasTheChronologicalEdges(boolean arriveBy) {
    var state = search(arriveBy);
    var path = (DefaultStreetPath) DefaultStreetSearchResult.of(state).toPath();

    var stored = DefaultStreetSearchResult.of(state).toStoredPath();

    assertEquals(path.edges(), stored.edges());
    assertEquals(path.distanceMeters(), stored.distanceMeters(), 0.001);
    assertFalse(stored.isEmpty());
  }

  @Test
  void reTraverseAStoredPath() {
    var state = search(false);
    var stored = DefaultStreetSearchResult.of(state).toStoredPath();

    var path = (DefaultStreetPath) stored.reTraverse(request(false)).orElseThrow();

    assertEquals(stored.edges(), path.edges());
    assertEquals(Duration.ofSeconds(state.getElapsedTimeSeconds()), path.duration());
    assertEquals(state.getWeight(), path.generalizedCost(), 0.001);
  }

  @Test
  void aPathIsItsOwnResult() {
    var path = DefaultStreetSearchResult.of(search(false)).toPath();
    assertSame(path, path.toPath());
  }

  @Test
  void emptyStoredPath() {
    assertTrue(StoredStreetPath.EMPTY.isEmpty());
    assertSame(StreetPath.EMPTY, StoredStreetPath.EMPTY.reTraverse(request(false)).orElseThrow());
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
