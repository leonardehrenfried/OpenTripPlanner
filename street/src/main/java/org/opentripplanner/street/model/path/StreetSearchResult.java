package org.opentripplanner.street.model.path;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.opentripplanner.street.model.edge.Edge;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.search.state.State;

/// The result of a street search: the final state of the search, in search order.
///
/// Everything this class offers is cheap, because it is read from the final state or found by
/// walking the back-state chain once. Use it for the many candidates a search produces, like the
/// stops found by a nearby stop search or the access and egress candidates of a transit search.
///
/// The chronological [StreetPath], which is needed for the legs of an itinerary, is only built by
/// [#toPath()]. For arriveBy searches the back-state chain runs forward in time, so building the
/// path means reversing it, which re-traverses every edge. This is the only place that does so,
/// which makes the cost visible at the call site.
///
/// A search result holds the search states and is deliberately not serializable. Use
/// [#toStoredPath()] for paths which are kept, like transfers stored in the graph.
public final class StreetSearchResult {

  private final State finalState;

  private StreetSearchResult(State finalState) {
    this.finalState = Objects.requireNonNull(finalState);
  }

  public static StreetSearchResult of(State finalState) {
    return new StreetSearchResult(finalState);
  }

  /// Whether the search ran backward in time, so the final state is where the path starts.
  public boolean isArriveBy() {
    return finalState.getRequest().arriveBy();
  }

  /// The generalized cost (A* weight) of the search.
  public double generalizedCost() {
    return finalState.getWeight();
  }

  /// The time it takes to traverse the path, rounded up to whole seconds.
  public Duration duration() {
    return Duration.ofSeconds(finalState.getElapsedTimeSeconds());
  }

  /// The distance traversed in meters.
  public double traversalDistance() {
    return finalState.getTraversalDistanceMeters();
  }

  /// The vertex where the search ended. In chronological order this is the end of the path for
  /// depart-after searches and the start for arriveBy searches.
  public Vertex finalVertex() {
    return finalState.getVertex();
  }

  /// True if any part of the path is traversed by car.
  public boolean containsModeCar() {
    return finalState.containsModeCar();
  }

  /// True if all edges are traversed on foot.
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
  /// of an itinerary.
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
