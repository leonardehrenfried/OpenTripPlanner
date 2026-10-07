package org.opentripplanner.ext.carpooling.routing;

import java.time.Duration;
import java.util.List;
import org.locationtech.jts.geom.LineString;
import org.opentripplanner.street.geometry.GeometryUtils;
import org.opentripplanner.street.model.path.StreetLeg;
import org.opentripplanner.street.model.path.StreetPath;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.search.state.State;

/**
 * A street path routed by the carpooling sandbox, like a walk to the pickup or a segment of the
 * driver's route.
 * <p>
 * Besides the {@link StreetPath} itself this keeps the endpoints of the path, which are needed
 * even if the path is empty, for example when the pickup is at the driver's position.
 */
public final class CarpoolPath {

  /**
   * The paths are only used for their geometry and distance, so the elevation offset does not
   * matter.
   */
  private static final double NO_ELEVATION_OFFSET = 0;

  private final StreetPath path;
  private final double weight;
  private final Vertex from;
  private final Vertex to;

  private CarpoolPath(State searchState) {
    this.path = StreetPath.of(searchState);
    this.weight = searchState.getWeight();
    var origin = searchOrigin(searchState).getVertex();
    if (searchState.getRequest().arriveBy()) {
      this.from = searchState.getVertex();
      this.to = origin;
    } else {
      this.from = origin;
      this.to = searchState.getVertex();
    }
  }

  /**
   * @param searchState The final state of a street search. For arriveBy searches the path is
   *                    reversed, so it always runs forward in time.
   */
  public static CarpoolPath of(State searchState) {
    return new CarpoolPath(searchState);
  }

  /**
   * The vertex where the path starts, in chronological order.
   */
  public Vertex from() {
    return from;
  }

  /**
   * The vertex where the path ends, in chronological order.
   */
  public Vertex to() {
    return to;
  }

  /**
   * The time it takes to traverse the path, see {@link StreetPath#duration()}.
   */
  public Duration duration() {
    return path.duration();
  }

  /**
   * The A* weight of the path. It already accounts for the preferences of the search that
   * produced it, like walk reluctance.
   */
  public double weight() {
    return weight;
  }

  public LineString geometry() {
    return GeometryUtils.concatenateLineStrings(streetLegs(), StreetLeg::geometry);
  }

  public double distanceMeters() {
    return streetLegs().stream().mapToDouble(StreetLeg::distanceMeters).sum();
  }

  public StreetPath streetPath() {
    return path;
  }

  @Override
  public String toString() {
    return "CarpoolPath{" + from + " -> " + to + ", " + duration() + "}";
  }

  private List<StreetLeg> streetLegs() {
    return path
      .legs(NO_ELEVATION_OFFSET)
      .stream()
      .filter(StreetLeg.class::isInstance)
      .map(StreetLeg.class::cast)
      .toList();
  }

  private static State searchOrigin(State state) {
    var origin = state;
    while (origin.getBackState() != null) {
      origin = origin.getBackState();
    }
    return origin;
  }
}
