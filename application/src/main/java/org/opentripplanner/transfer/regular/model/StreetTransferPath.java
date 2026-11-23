package org.opentripplanner.transfer.regular.model;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import org.opentripplanner.core.model.id.FeedScopedId;
import org.opentripplanner.place.api.NearbyStop;
import org.opentripplanner.street.model.path.StreetPath;
import org.opentripplanner.street.model.path.StreetSearchResult;

/**
 * The street path of a regular transfer to a stop, as it is stored in the graph.
 * <p>
 * The path holds no states of the street search which found it, so it can be serialized and does
 * not keep a search in memory. Its times and costs depend on the preferences of a search, use
 * {@link StreetPath#reTraverse} to get them for a specific request.
 * <p>
 * This is the path type of the regular transfers used by Raptor, which are found and stored by the
 * regular-transfer pipeline. That pipeline keys the transfers by profile, mode and from-stop, so
 * this type only holds the stop the transfer leads to, its distance and its path.
 * <p>
 * The flex transfers are stored as {@link PathTransfer}s, which hold the same street path together
 * with their stops and the modes they are valid for.
 * <p>
 * <pre>
 * TODO: This type and {@link PathTransfer} will be unified by moving the flex transfers to the
 *       regular-transfer pipeline, see {@link PathTransfer}.
 * </pre>
 */
public final class StreetTransferPath implements Serializable {

  private final FeedScopedId stopId;
  private final double distance;
  private final StreetPath path;

  /**
   * @param stopId   the stop the transfer leads to
   * @param distance the distance of the transfer in meters
   * @param path     the street path or search result of the transfer, or {@link StreetPath#EMPTY}
   *                 if it has no path, like a straight-line transfer. Only the path without the
   *                 search states is kept.
   */
  public StreetTransferPath(FeedScopedId stopId, double distance, StreetSearchResult path) {
    this.stopId = Objects.requireNonNull(stopId);
    this.distance = distance;
    this.path = Objects.requireNonNull(path).withoutStates();
  }

  /**
   * Create a transfer without a street path, like a straight-line transfer.
   */
  public StreetTransferPath(FeedScopedId stopId, double distance) {
    this(stopId, distance, StreetPath.EMPTY);
  }

  /**
   * Create the transfer to a stop found by a street search. The states of the search are dropped.
   */
  public static StreetTransferPath of(NearbyStop nearbyStop) {
    return new StreetTransferPath(nearbyStop.stopId(), nearbyStop.distance(), nearbyStop.result());
  }

  /** The stop the transfer leads to. */
  public FeedScopedId stopId() {
    return stopId;
  }

  /** The distance of the transfer in meters. */
  public double distance() {
    return distance;
  }

  /**
   * The street path of the transfer, without search states, or {@link StreetPath#EMPTY} if the
   * transfer has no path, like a straight-line transfer.
   */
  public StreetPath path() {
    return path;
  }

  /** Convert to a {@link NearbyStop}, for the APIs which return transfers as nearby stops. */
  public NearbyStop toNearbyStop() {
    return new NearbyStop(stopId, distance, path);
  }

  @Override
  public boolean equals(Object o) {
    return (
      o instanceof StreetTransferPath other &&
      Double.compare(distance, other.distance) == 0 &&
      stopId.equals(other.stopId) &&
      path.equals(other.path)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(stopId, distance, path);
  }

  @Override
  public String toString() {
    return String.format(
      Locale.ROOT,
      "StreetTransferPath{stop %s at %.1f meters%s}",
      stopId,
      distance,
      path.isEmpty() ? "" : " w/path"
    );
  }
}
