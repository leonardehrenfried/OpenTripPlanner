package org.opentripplanner.street.model.path;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.locationtech.jts.geom.LineString;
import org.opentripplanner.street.geometry.GeometryUtils;
import org.opentripplanner.street.model.edge.Edge;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.search.request.StreetSearchRequest;

/// A path which is not the result of a street search, see [StreetPath#EMPTY].
///
/// It is an enum so the instance stays a singleton when it is deserialized as part of the graph.
enum EmptyStreetPath implements StreetPath {
  INSTANCE;

  private static final String NO_SEARCH_MESSAGE =
    "The empty path is not the result of a street search.";

  @Override
  public double generalizedCost() {
    return 0;
  }

  @Override
  public Duration duration() {
    return Duration.ZERO;
  }

  @Override
  public ElevationChange elevationChange() {
    return new ElevationChange(0, 0);
  }

  @Override
  public boolean arrivesWithRentedVehicleFromStation() {
    return false;
  }

  @Override
  public List<StreetLeg> legs(double ellipsoidToGeoidDifference) {
    return List.of();
  }

  @Override
  public double traversalDistance() {
    return 0;
  }

  @Override
  public Optional<StreetPath> reTraverse(StreetSearchRequest request) {
    return Optional.of(this);
  }

  @Override
  public Optional<StreetPath> continueSearch(Edge edge) {
    throw new UnsupportedOperationException(NO_SEARCH_MESSAGE);
  }

  @Override
  public Optional<StreetPath> continueSearch(StreetPath path) {
    throw new UnsupportedOperationException(NO_SEARCH_MESSAGE);
  }

  @Override
  public Vertex fromVertex() {
    throw new UnsupportedOperationException(NO_SEARCH_MESSAGE);
  }

  @Override
  public Vertex toVertex() {
    throw new UnsupportedOperationException(NO_SEARCH_MESSAGE);
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public LineString geometry() {
    return GeometryUtils.getGeometryFactory().createLineString();
  }

  @Override
  public boolean containsOnlyWalkMode() {
    return true;
  }

  @Override
  public boolean containsModeCar() {
    return false;
  }

  @Override
  public String toString() {
    return "EmptyStreetPath";
  }
}
