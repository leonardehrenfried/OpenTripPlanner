package org.opentripplanner.street.model.path;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.locationtech.jts.geom.LineString;
import org.opentripplanner.street.geometry.GeometryUtils;
import org.opentripplanner.street.model.edge.Edge;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.street.search.request.StreetSearchRequest;

/// A path made up of paths which follow each other in time, see [StreetPath#concat(StreetPath...)].
///
/// The paths are not necessarily the result of the same street search. One of them may not be
/// part of the street network at all, like a ride on a flexible trip. Therefore, the search can't
/// be continued or repeated.
final class CompositeStreetPath implements StreetPath {

  private static final String NOT_SUPPORTED_MESSAGE =
    "A composite path is not the result of a single street search.";

  private final List<StreetPath> paths;
  private final Duration duration;

  CompositeStreetPath(List<StreetPath> paths) {
    if (paths.isEmpty()) {
      throw new IllegalArgumentException("A composite path needs at least one path");
    }
    this.paths = List.copyOf(paths);
    // Computed from the start and end time, rather than adding up the rounded durations of the
    // paths, the same way as for a single street search.
    long millis = Duration.between(startTime(), endTime()).toMillis();
    this.duration = Duration.ofSeconds((millis + 999L) / 1000L);
  }

  @Override
  public double generalizedCost() {
    return paths.stream().mapToDouble(StreetPath::generalizedCost).sum();
  }

  @Override
  public Duration duration() {
    return duration;
  }

  @Override
  public Instant startTime() {
    return paths.getFirst().startTime();
  }

  @Override
  public Instant endTime() {
    return paths.getLast().endTime();
  }

  @Override
  public ElevationChange elevationChange() {
    double gained = 0;
    double lost = 0;
    for (var path : paths) {
      var change = path.elevationChange();
      gained += change.elevationGainedMeters();
      lost += change.elevationLostMeters();
    }
    return new ElevationChange(gained, lost);
  }

  @Override
  public boolean arrivesWithRentedVehicleFromStation() {
    return paths.getLast().arrivesWithRentedVehicleFromStation();
  }

  @Override
  public List<StreetLeg> computeLegs(double ellipsoidToGeoidDifference) {
    return paths
      .stream()
      .flatMap(path -> path.computeLegs(ellipsoidToGeoidDifference).stream())
      .toList();
  }

  @Override
  public double traversalDistance() {
    return paths.stream().mapToDouble(StreetPath::traversalDistance).sum();
  }

  @Override
  public Optional<StreetPath> reTraverse(StreetSearchRequest request) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_MESSAGE);
  }

  @Override
  public Optional<StreetPath> continueSearch(Edge edge) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_MESSAGE);
  }

  @Override
  public Optional<StreetPath> continueSearch(StreetPath path) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_MESSAGE);
  }

  @Override
  public Optional<StreetPath> continueSearchAfter(Duration gap, StreetPath path) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_MESSAGE);
  }

  @Override
  public Vertex fromVertex() {
    return paths.getFirst().fromVertex();
  }

  @Override
  public Vertex toVertex() {
    return paths.getLast().toVertex();
  }

  @Override
  public boolean isEmpty() {
    return paths.stream().allMatch(StreetPath::isEmpty);
  }

  @Override
  public LineString geometry() {
    var geometries = paths
      .stream()
      .filter(path -> !path.isEmpty())
      .map(StreetPath::geometry);
    return GeometryUtils.concatenateLineStrings(geometries::iterator);
  }

  @Override
  public boolean containsOnlyWalkMode() {
    return paths.stream().allMatch(StreetPath::containsOnlyWalkMode);
  }

  @Override
  public boolean containsModeCar() {
    return paths.stream().anyMatch(StreetPath::containsModeCar);
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof CompositeStreetPath other && paths.equals(other.paths);
  }

  @Override
  public int hashCode() {
    return paths.hashCode();
  }

  @Override
  public String toString() {
    return "CompositeStreetPath" + paths;
  }
}
