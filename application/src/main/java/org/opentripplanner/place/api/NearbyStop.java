package org.opentripplanner.place.api;

import java.time.Duration;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Nullable;
import org.opentripplanner.core.model.id.FeedScopedId;
import org.opentripplanner.street.model.path.DefaultStreetPath;
import org.opentripplanner.street.model.path.StreetPath;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.search.state.State;

/**
 * A specific stop at a distance. Also includes the street path of how to reach the stop from the
 * search origin, if the stop was found by a street search.
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
   * The street path between the search origin and the stop, or null if the stop was not found by
   * a street search.
   */
  @Nullable
  private final StreetPath path;

  /**
   * @param vertex The vertex where the street search reached the stop
   * @param path   The street path between the search origin and the stop
   */
  public NearbyStop(
    FeedScopedId stopId,
    double distance,
    @Nullable Vertex vertex,
    @Nullable StreetPath path
  ) {
    this.stopId = Objects.requireNonNull(stopId);
    this.distance = distance;
    this.vertex = vertex;
    this.path = path;
  }

  /**
   * Create a NearbyStop which was not found by a street search, like with a straight-line search.
   */
  public NearbyStop(FeedScopedId stopId, double distance) {
    this(stopId, distance, null, null);
  }

  /**
   * Create a NearbyStop with a pre-computed street path, like a transfer between stops.
   */
  public NearbyStop(FeedScopedId stopId, double distance, @Nullable StreetPath path) {
    this(stopId, distance, null, path);
  }

  /**
   * Given a State at a StopVertex, bundle the stop's id together with information about how far
   * away it is and the path leading up to the given State.
   */
  public static NearbyStop nearbyStopForState(State state, FeedScopedId stopId) {
    var path = new DefaultStreetPath(state);
    return new NearbyStop(stopId, path.traversalDistance(), state.getVertex(), path);
  }

  /**
   * Create a NearbyStop with zero distance and no edges.
   */
  public static NearbyStop ofZeroDistance(FeedScopedId stopId, State state) {
    return new NearbyStop(stopId, 0d, state.getVertex(), new DefaultStreetPath(state));
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
    if ((this.path == null) != (that.path == null)) {
      throw new IllegalStateException(
        "Only NearbyStops which both contain or lack a path may be compared."
      );
    }

    if (this.path != null) {
      return (int) this.path.generalizedCost() - (int) that.path.generalizedCost();
    }
    return (int) this.distance - (int) that.distance;
  }

  /**
   * Duration it took to reach the stop.
   */
  public Duration duration() {
    return path.duration();
  }

  @Override
  public int hashCode() {
    return Objects.hash(stopId, distance, path);
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
      Objects.equals(path, that.path)
    );
  }

  public String toString() {
    return String.format(
      Locale.ROOT,
      "stop %s at %.1f meters%s",
      stopId,
      distance,
      path != null ? " w/path" : ""
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

  @Nullable
  public StreetPath path() {
    return path;
  }
}
