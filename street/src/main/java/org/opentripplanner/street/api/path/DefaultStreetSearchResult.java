package org.opentripplanner.street.api.path;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.opentripplanner.street.api.request.StreetSearchRequest;
import org.opentripplanner.street.model.edge.Edge;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.search.state.EdgeTraverser;
import org.opentripplanner.street.search.state.State;
import org.opentripplanner.street.search.state.StateEditor;

/// A [StreetSearchResult] backed by the final state of a street search, in search order.
///
/// Everything except [#toPath()] is cheap, because it is read from the final state or found by
/// walking the back-state chain once. For arriveBy searches the back-state chain runs backward
/// along the path, so building the chronological path means reversing it, which re-traverses
/// every edge. [#toPath()] is the only place that does so.
///
/// It holds the search states and is deliberately not serializable.
public final class DefaultStreetSearchResult implements StreetSearchResult {

  private final State finalState;

  private DefaultStreetSearchResult(State finalState) {
    this.finalState = Objects.requireNonNull(finalState);
  }

  public static DefaultStreetSearchResult of(State finalState) {
    return new DefaultStreetSearchResult(finalState);
  }

  /// Whether the search ran backward in time, so the final state is where the path starts.
  public boolean isArriveBy() {
    return finalState.getRequest().arriveBy();
  }

  @Override
  public double generalizedCost() {
    return finalState.getWeight();
  }

  @Override
  public Duration duration() {
    return Duration.ofSeconds(finalState.getElapsedTimeSeconds());
  }

  /// The start of the path. For arriveBy searches this is the time of the final state, otherwise
  /// the start time of the search.
  @Override
  public Instant startTime() {
    return isArriveBy() ? timeOf(finalState) : searchStartTime();
  }

  /// The end of the path. For arriveBy searches this is the start time of the search, otherwise
  /// the time of the final state.
  @Override
  public Instant endTime() {
    return isArriveBy() ? searchStartTime() : timeOf(finalState);
  }

  @Override
  public double traversalDistance() {
    return finalState.getTraversalDistanceMeters();
  }

  /// The vertex where the search ended. In chronological order this is the end of the path for
  /// depart-after searches and the start for arriveBy searches.
  public Vertex finalVertex() {
    return finalState.getVertex();
  }

  @Override
  public boolean containsModeCar() {
    return finalState.containsModeCar();
  }

  @Override
  public boolean containsOnlyWalkMode() {
    return finalState.containsOnlyWalkMode();
  }

  /// Continue the street search along the edges of `other`, after a gap which is not part of the
  /// street network, like a ride on a flexible trip. The search is restarted with the same request
  /// at the start of `other`, `gap` after this result ends. For arriveBy searches it is restarted
  /// at the end of `other`, `gap` before this result starts. Since a search starts at a whole
  /// second, the time of the final state is rounded in the direction of the search first.
  ///
  /// The returned result contains only the edges of `other`. Use
  /// [StreetPath#concat(StreetSearchResult...)] to combine it with this result and the gap.
  ///
  /// @param other a [StreetPath] or another [DefaultStreetSearchResult]; its states are not
  ///              reversed, only its edges are used
  /// @return Empty if the edges can't be traversed
  public Optional<StreetSearchResult> continueSearchAfter(Duration gap, StreetSearchResult other) {
    boolean arriveBy = isArriveBy();
    var restartTime = arriveBy ? finalState.getTime().minus(gap) : finalState.getTime().plus(gap);
    var request = StreetSearchRequest.copyOf(finalState.getRequest())
      .withStartTime(restartTime)
      .build();
    var segment = Segment.of(other);
    var startState = new StateEditor(arriveBy ? segment.to() : segment.from(), request).makeState();
    var edgesInSearchOrder = arriveBy ? segment.edges().reversed() : segment.edges();
    return EdgeTraverser.traverseEdges(startState, edgesInSearchOrder).map(
      DefaultStreetSearchResult::of
    );
  }

  /// Build the chronological path. For arriveBy searches this reverses the state chain, which is
  /// expensive, so only call it for results which are actually used, like when mapping the legs
  /// of an itinerary. Every call builds a new path.
  @Override
  public StreetPath toPath() {
    var chronologicalFinalState = isArriveBy() ? finalState.reverse() : finalState;
    var states = new ArrayList<State>();
    for (State s : chronologicalFinalState.listBackStates()) {
      states.add(s);
    }
    Collections.reverse(states);
    var edges = new ArrayList<Edge>(states.size() - 1);
    for (int i = 1; i < states.size(); i++) {
      edges.add(states.get(i).getBackEdge());
    }
    return new DefaultStreetPath(states, edges);
  }

  @Override
  public StreetPath withoutStates() {
    var segment = segment();
    return new DefaultStreetPath(
      segment.from(),
      segment.to(),
      segment.edges(),
      startTime(),
      endTime(),
      generalizedCost(),
      duration(),
      traversalDistance()
    );
  }

  /// Two results are equal if they wrap the same final state of a search.
  @Override
  public boolean equals(Object o) {
    return o instanceof DefaultStreetSearchResult other && finalState == other.finalState;
  }

  @Override
  public int hashCode() {
    return System.identityHashCode(finalState);
  }

  @Override
  public String toString() {
    return "DefaultStreetSearchResult{" + finalState + "}";
  }

  private Instant searchStartTime() {
    return Instant.ofEpochMilli(finalState.getRequest().startTime().toEpochMilli());
  }

  private static Instant timeOf(State state) {
    return Instant.ofEpochMilli(state.getTimeMilliseconds());
  }

  /// The edges of this result in chronological order, with the vertices where it starts and ends,
  /// found by walking the back-state chain once without reversing it.
  private Segment segment() {
    var edges = new ArrayList<Edge>();
    var origin = finalState;
    while (origin.getBackState() != null) {
      edges.add(origin.getBackEdge());
      origin = origin.getBackState();
    }
    if (isArriveBy()) {
      // The back-state chain of an arriveBy search already runs forward in time
      return new Segment(finalState.getVertex(), origin.getVertex(), edges);
    }
    Collections.reverse(edges);
    return new Segment(origin.getVertex(), finalState.getVertex(), edges);
  }

  /// The chronological edges of a path or search result and the vertices where it starts and ends.
  private record Segment(Vertex from, Vertex to, List<Edge> edges) {
    static Segment of(StreetSearchResult result) {
      return switch (result) {
        case DefaultStreetPath path -> new Segment(
          path.fromVertex(),
          path.toVertex(),
          path.edges()
        );
        case DefaultStreetSearchResult searchResult -> searchResult.segment();
        default -> throw new IllegalArgumentException("A search can't continue along " + result);
      };
    }
  }
}
