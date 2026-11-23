package org.opentripplanner.place.api;

import java.time.Duration;
import java.util.Locale;
import java.util.Objects;
import org.jspecify.annotations.Nullable;
import org.opentripplanner.core.model.id.FeedScopedId;
import org.opentripplanner.street.api.path.DefaultStreetSearchResult;
import org.opentripplanner.street.api.path.StreetPath;
import org.opentripplanner.street.api.path.StreetSearchResult;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.search.state.State;

/**
 * A specific stop at a distance, found by a search from an origin. If the stop was found by a
 * street search, it also includes the result of that search.
 * <p>
 * The cost and duration of the result are cheap to read, so there can be many nearby stops. The
 * street path is only built by {@link StreetSearchResult#toPath()}, for the stops which are used,
 * like when mapping an itinerary.
 */
public class NearbyStop implements Comparable<NearbyStop> {

  private final FeedScopedId stopId;
  private final double distance;

  /**
   * The vertex where the street search reached the stop, or null if the stop was not found by a
   * street search.
   */
  @Nullable
  private final Vertex vertex;

  /**
   * The result of the street search between the search origin and the stop, or
   * {@link StreetPath#EMPTY} if the stop was not found by a street search.
   */
  private final StreetSearchResult result;

  /**
   * @param vertex The vertex where the street search reached the stop
   * @param result The result of the street search between the search origin and the stop
   */
  public NearbyStop(
    FeedScopedId stopId,
    double distance,
    @Nullable Vertex vertex,
    StreetSearchResult result
  ) {
    this.stopId = Objects.requireNonNull(stopId);
    this.distance = distance;
    this.vertex = vertex;
    this.result = Objects.requireNonNull(result);
  }

  /**
   * Create a NearbyStop which was not found by a street search, like with a straight-line search.
   */
  public NearbyStop(FeedScopedId stopId, double distance) {
    this(stopId, distance, null, StreetPath.EMPTY);
  }

  /**
   * Create a NearbyStop with a pre-computed street path, like a transfer between stops.
   */
  public NearbyStop(FeedScopedId stopId, double distance, StreetSearchResult result) {
    this(stopId, distance, null, result);
  }

  /**
   * Given a State at a StopVertex, bundle the stop's id together with information about how far
   * away it is and the path leading up to the given State.
   */
  public static NearbyStop nearbyStopForState(State state, FeedScopedId stopId) {
    var result = DefaultStreetSearchResult.of(state);
    return new NearbyStop(stopId, result.traversalDistance(), state.getVertex(), result);
  }

  /**
   * Create a NearbyStop with zero distance and no edges.
   */
  public static NearbyStop ofZeroDistance(FeedScopedId stopId, State state) {
    return new NearbyStop(stopId, 0d, state.getVertex(), DefaultStreetSearchResult.of(state));
  }

  /**
   * Return {@code true} if this instance has a lower weight/cost than the given {@code other}.
   * If the path is not set, the distance is used for comparison instead. If the
   * weight/cost/distance is equals (or worse) this method returns {@code false}.
   */
  public boolean isBetter(NearbyStop other) {
    return compareTo(other) < 0;
  }

  @Override
  public int compareTo(NearbyStop that) {
    if (this.hasPath() != that.hasPath()) {
      throw new IllegalStateException(
        "Only NearbyStops which both contain or lack a path may be compared."
      );
    }

    if (this.hasPath()) {
      return (int) this.result.generalizedCost() - (int) that.result.generalizedCost();
    }
    return (int) this.distance - (int) that.distance;
  }

  /**
   * Duration it took to reach the stop.
   */
  public Duration duration() {
    return result.duration();
  }

  @Override
  public int hashCode() {
    return Objects.hash(stopId, distance, result);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final NearbyStop that = (NearbyStop) o;
    return (
      Double.compare(that.distance, distance) == 0 &&
      stopId.equals(that.stopId) &&
      Objects.equals(result, that.result)
    );
  }

  public String toString() {
    return String.format(
      Locale.ROOT,
      "stop %s at %.1f meters%s",
      stopId,
      distance,
      hasPath() ? " w/path" : ""
    );
  }

  public FeedScopedId stopId() {
    return stopId;
  }

  public double distance() {
    return distance;
  }

  @Nullable
  public Vertex vertex() {
    return vertex;
  }

  /**
   * The result of the street search between the search origin and the stop, or
   * {@link StreetPath#EMPTY} if the stop was not found by a street search. Reading its cost and
   * duration is cheap.
   */
  public StreetSearchResult result() {
    return result;
  }

  private boolean hasPath() {
    return result != StreetPath.EMPTY;
  }
}
