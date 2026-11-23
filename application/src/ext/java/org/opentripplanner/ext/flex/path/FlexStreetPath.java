package org.opentripplanner.ext.flex.path;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.locationtech.jts.geom.LineString;
import org.opentripplanner.core.model.id.FeedScopedId;
import org.opentripplanner.ext.flex.FlexParameters;
import org.opentripplanner.ext.flex.flexpathcalculator.FlexPath;
import org.opentripplanner.ext.flex.trip.FlexTrip;
import org.opentripplanner.street.api.path.ElevationChange;
import org.opentripplanner.street.api.path.StreetLeg;
import org.opentripplanner.street.api.path.StreetPath;
import org.opentripplanner.street.api.request.StreetSearchRequest;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.utils.tostring.ToStringBuilder;

/**
 * A ride on a flexible trip from one vertex to another. It is not part of the street network, but
 * it is a {@link StreetPath}, so that it can be combined with the walking paths before and after
 * it using {@link StreetPath#concat(StreetPath...)}.
 * <p>
 * It consists of a single {@link FlexStreetLeg}.
 */
public final class FlexStreetPath implements StreetPath, Serializable {

  private static final String NOT_SUPPORTED_MESSAGE =
    "A flex path is not the result of a street search.";

  private final Vertex fromVertex;
  private final Vertex toVertex;
  private final FeedScopedId fromStopId;
  private final FeedScopedId toStopId;
  private final FlexTrip<?, ?> trip;
  private final int boardStopPosInPattern;
  private final int alightStopPosInPattern;
  private final LocalDate serviceDate;
  private final FlexPath flexPath;
  private final double generalizedCost;
  private final Instant startTime;
  private final boolean arriveBy;

  /**
   * @param startTime The time the ride starts with millisecond precision
   * @param arriveBy  Whether the path is part of an arriveBy search. The times of the leg are
   *                  rounded to whole seconds in the same way as the times of the street legs
   *                  next to it.
   */
  public FlexStreetPath(
    Vertex fromVertex,
    Vertex toVertex,
    FeedScopedId fromStopId,
    FeedScopedId toStopId,
    FlexTrip<?, ?> trip,
    int boardStopPosInPattern,
    int alightStopPosInPattern,
    LocalDate serviceDate,
    FlexPath flexPath,
    FlexParameters flexParameters,
    Instant startTime,
    boolean arriveBy
  ) {
    this.fromVertex = Objects.requireNonNull(fromVertex);
    this.toVertex = Objects.requireNonNull(toVertex);
    this.fromStopId = fromStopId;
    this.toStopId = toStopId;
    this.trip = trip;
    this.boardStopPosInPattern = boardStopPosInPattern;
    this.alightStopPosInPattern = alightStopPosInPattern;
    this.serviceDate = serviceDate;
    this.flexPath = Objects.requireNonNull(flexPath);
    this.generalizedCost =
      flexParameters.reluctance() * flexPath.durationSeconds + flexParameters.boardCost();
    this.startTime = Objects.requireNonNull(startTime);
    this.arriveBy = arriveBy;
  }

  public FeedScopedId fromStopId() {
    return fromStopId;
  }

  public FeedScopedId toStopId() {
    return toStopId;
  }

  public FlexTrip<?, ?> flexTrip() {
    return trip;
  }

  public int boardStopPosInPattern() {
    return boardStopPosInPattern;
  }

  public int alightStopPosInPattern() {
    return alightStopPosInPattern;
  }

  public LocalDate serviceDate() {
    return serviceDate;
  }

  public int durationSeconds() {
    return flexPath.durationSeconds;
  }

  public double distanceMeters() {
    return flexPath.distanceMeters;
  }

  @Override
  public double generalizedCost() {
    return generalizedCost;
  }

  @Override
  public Duration duration() {
    return Duration.ofSeconds(flexPath.durationSeconds);
  }

  @Override
  public Instant startTime() {
    return startTime;
  }

  @Override
  public Instant endTime() {
    return startTime.plus(duration());
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
  public List<StreetLeg> computeLegs(double ellipsoidToGeoidDifference) {
    return List.of(new FlexStreetLeg(this));
  }

  @Override
  public double traversalDistance() {
    return flexPath.distanceMeters;
  }

  /// A flex path holds no search states, so it is returned as is.
  @Override
  public StreetPath withoutStates() {
    return this;
  }

  @Override
  public Optional<StreetPath> reTraverse(StreetSearchRequest request) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_MESSAGE);
  }

  @Override
  public Vertex fromVertex() {
    return fromVertex;
  }

  @Override
  public Vertex toVertex() {
    return toVertex;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public LineString geometry() {
    return flexPath.getGeometry();
  }

  @Override
  public boolean containsOnlyWalkMode() {
    return false;
  }

  @Override
  public boolean containsModeCar() {
    return false;
  }

  @Override
  public String toString() {
    return ToStringBuilder.of(FlexStreetPath.class)
      .addObj("from", fromVertex)
      .addObj("to", toVertex)
      .addObj("trip", trip == null ? null : trip.getId())
      .addDateTime("startTime", startTime)
      .addNum("durationSeconds", flexPath.durationSeconds)
      .toString();
  }

  /**
   * The start time of the leg, rounded to whole seconds in the same way as the times of street
   * legs.
   */
  Instant legStartTime() {
    long millis = startTime.toEpochMilli();
    long seconds = arriveBy ? Math.floorDiv(millis, 1000L) : Math.floorDiv(millis + 999L, 1000L);
    return Instant.ofEpochSecond(seconds);
  }
}
