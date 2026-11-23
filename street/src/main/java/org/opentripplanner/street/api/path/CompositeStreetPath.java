package org.opentripplanner.street.api.path;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
import org.locationtech.jts.geom.LineString;
import org.opentripplanner.street.api.request.StreetSearchRequest;
import org.opentripplanner.street.geometry.GeometryUtils;
import org.opentripplanner.street.model.vertex.Vertex;

/// A path made up of paths or search results which follow each other in time, see
/// [StreetPath#concat(StreetSearchResult...)].
///
/// The parts are not necessarily the result of the same street search. One of them may not be
/// part of the street network at all, like a ride on a flexible trip. Therefore, the search can't
/// be continued or repeated.
///
/// The cost, duration, times and modes are read from the parts, which is cheap. The parts are only
/// turned into paths, once, when the details of the path are needed, like for the legs.
final class CompositeStreetPath implements StreetPath, Serializable {

  private static final String NOT_SUPPORTED_MESSAGE =
    "A composite path is not the result of a single street search.";

  /// The parts may hold the states of a search, which are not serializable. They are transient
  /// because a composite path is built for a request and never stored: a stored path is always
  /// created with [#withoutStates()], which a composite path does not support.
  private final transient List<StreetSearchResult> parts;
  private final Duration duration;

  /// The parts as paths, built when they are first needed.
  @Nullable
  private transient List<StreetPath> paths;

  CompositeStreetPath(List<StreetSearchResult> parts) {
    if (parts.isEmpty()) {
      throw new IllegalArgumentException("A composite path needs at least one part");
    }
    this.parts = List.copyOf(parts);
    // Computed from the start and end time, rather than adding up the rounded durations of the
    // paths, the same way as for a single street search.
    long millis = Duration.between(startTime(), endTime()).toMillis();
    this.duration = Duration.ofSeconds((millis + 999L) / 1000L);
  }

  @Override
  public double generalizedCost() {
    return parts.stream().mapToDouble(StreetSearchResult::generalizedCost).sum();
  }

  @Override
  public Duration duration() {
    return duration;
  }

  @Override
  public Instant startTime() {
    return parts.getFirst().startTime();
  }

  @Override
  public Instant endTime() {
    return parts.getLast().endTime();
  }

  @Override
  public ElevationChange elevationChange() {
    double gained = 0;
    double lost = 0;
    for (var path : paths()) {
      var change = path.elevationChange();
      gained += change.elevationGainedMeters();
      lost += change.elevationLostMeters();
    }
    return new ElevationChange(gained, lost);
  }

  @Override
  public boolean arrivesWithRentedVehicleFromStation() {
    return paths().getLast().arrivesWithRentedVehicleFromStation();
  }

  @Override
  public List<StreetLeg> computeLegs(double ellipsoidToGeoidDifference) {
    return paths()
      .stream()
      .flatMap(path -> path.computeLegs(ellipsoidToGeoidDifference).stream())
      .toList();
  }

  @Override
  public double traversalDistance() {
    return parts.stream().mapToDouble(StreetSearchResult::traversalDistance).sum();
  }

  @Override
  public StreetPath withoutStates() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_MESSAGE);
  }

  @Override
  public Optional<StreetPath> reTraverse(StreetSearchRequest request) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_MESSAGE);
  }

  @Override
  public Vertex fromVertex() {
    return paths().getFirst().fromVertex();
  }

  @Override
  public Vertex toVertex() {
    return paths().getLast().toVertex();
  }

  @Override
  public boolean isEmpty() {
    return paths().stream().allMatch(StreetPath::isEmpty);
  }

  @Override
  public LineString geometry() {
    var geometries = paths()
      .stream()
      .filter(path -> !path.isEmpty())
      .map(StreetPath::geometry);
    return GeometryUtils.concatenateLineStrings(geometries::iterator);
  }

  @Override
  public boolean containsOnlyWalkMode() {
    return parts.stream().allMatch(StreetSearchResult::containsOnlyWalkMode);
  }

  @Override
  public boolean containsModeCar() {
    return parts.stream().anyMatch(StreetSearchResult::containsModeCar);
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof CompositeStreetPath other && parts.equals(other.parts);
  }

  @Override
  public int hashCode() {
    return parts.hashCode();
  }

  @Override
  public String toString() {
    return "CompositeStreetPath" + parts;
  }

  private List<StreetPath> paths() {
    if (paths == null) {
      paths = parts.stream().map(StreetSearchResult::toPath).toList();
    }
    return paths;
  }
}
