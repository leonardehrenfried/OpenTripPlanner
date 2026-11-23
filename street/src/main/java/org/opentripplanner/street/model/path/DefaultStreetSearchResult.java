package org.opentripplanner.street.model.path;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.opentripplanner.street.model.edge.Edge;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.search.state.State;

/// A [StreetSearchResult] backed by the final state of a street search, in search order.
///
/// Everything except [#toPath()] is cheap, because it is read from the final state or found by
/// walking the back-state chain once. For arriveBy searches the back-state chain runs backward
/// along the path, so building the chronological path means reversing it, which re-traverses
/// every edge. [#toPath()] is the only place that does so.
///
/// It holds the search states and is deliberately not serializable. Use [#toStoredPath()] for
/// paths which are kept, like transfers stored in the graph.
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

  /// Whether the search ended while renting a vehicle picked up at a rental station. This refers
  /// to the final state in search order, which for arriveBy searches is the start of the path. Use
  /// [StreetPath#arrivesWithRentedVehicleFromStation()] for the end of the path in chronological
  /// order.
  public boolean endsRentingVehicleFromStation() {
    return finalState.isRentingVehicleFromStation();
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

  /// The edges of the path in chronological order, without the search states, so it can be kept
  /// and serialized. This walks the back-state chain once but does not reverse it.
  public StoredStreetPath toStoredPath() {
    var edges = new ArrayList<Edge>();
    var origin = finalState;
    while (origin.getBackState() != null) {
      edges.add(origin.getBackEdge());
      origin = origin.getBackState();
    }
    if (isArriveBy()) {
      // The back-state chain of an arriveBy search already runs forward in time
      return StoredStreetPath.of(finalState.getVertex(), origin.getVertex(), edges);
    }
    Collections.reverse(edges);
    return StoredStreetPath.of(origin.getVertex(), finalState.getVertex(), List.copyOf(edges));
  }
}
