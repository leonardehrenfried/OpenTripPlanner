package org.opentripplanner.graph_builder.module.geometry;

import org.locationtech.jts.geom.Coordinate;
import org.opentripplanner.street.geometry.SphericalDistanceLibrary;

/** TODO Move this stuff into the geometry library */
class IndexedLineSegment {

  private static final double RADIUS = SphericalDistanceLibrary.RADIUS_OF_EARTH_IN_M;

  /**
   * The safety factor of {@link #isCertainlyFartherThan}, which covers the approximations made by
   * {@link #distance}.
   */
  private static final double FARTHER_THAN_SAFETY_FACTOR = 1.2;

  /**
   * Segments longer than this are never considered by {@link #isCertainlyFartherThan}. The great
   * circle through a long segment bulges out of its bounding box, and so may the cross-track error
   * computed by {@link #distance}.
   */
  private static final double FARTHER_THAN_MAX_LINE_LENGTH = 10_000;

  /** Coordinates at higher absolute longitudes may be close across the antimeridian. */
  private static final double FARTHER_THAN_MAX_ABS_LONGITUDE = 170;

  private final double lineLength;
  /** The bearing from start to end, cached since it is needed for every distance computation. */
  private final double bearingToEnd;
  private final double minLat;
  private final double maxLat;
  private final double minLon;
  private final double maxLon;
  /** The cosine of the latitude of the end that is closest to a pole. */
  private final double cosMaxAbsLat;
  int index;
  Coordinate start;
  Coordinate end;

  public IndexedLineSegment(int index, Coordinate start, Coordinate end) {
    this.index = index;
    this.start = start;
    this.end = end;
    this.lineLength = SphericalDistanceLibrary.fastDistance(start, end);
    this.bearingToEnd = bearing(start, end);
    this.minLat = Math.min(start.y, end.y);
    this.maxLat = Math.max(start.y, end.y);
    this.minLon = Math.min(start.x, end.x);
    this.maxLon = Math.max(start.x, end.x);
    this.cosMaxAbsLat = Math.cos(Math.toRadians(Math.max(Math.abs(start.y), Math.abs(end.y))));
  }

  /**
   * A cheap check, against the bounding box of the segment, whether {@link #distance} is certainly
   * greater than {@code meters}. It may return false even if the distance is greater, so it can be
   * used to skip computing the distance for segments that are far away from a coordinate.
   */
  boolean isCertainlyFartherThan(Coordinate coord, double meters) {
    if (
      lineLength > FARTHER_THAN_MAX_LINE_LENGTH ||
      Math.abs(coord.x) > FARTHER_THAN_MAX_ABS_LONGITUDE ||
      Math.max(Math.abs(minLon), Math.abs(maxLon)) > FARTHER_THAN_MAX_ABS_LONGITUDE
    ) {
      return false;
    }
    double latMargin = Math.toDegrees((FARTHER_THAN_SAFETY_FACTOR * meters) / RADIUS);
    if (coord.y < minLat - latMargin || coord.y > maxLat + latMargin) {
      return true;
    }
    // a degree of longitude is shortest at the latitude closest to a pole
    double cosLat = Math.min(cosMaxAbsLat, Math.cos(Math.toRadians(coord.y)));
    double lonMargin = latMargin / cosLat;
    return coord.x < minLon - lonMargin || coord.x > maxLon + lonMargin;
  }

  public double fraction(Coordinate coord) {
    double cte = crossTrackError(coord);
    double distanceToStart = SphericalDistanceLibrary.fastDistance(coord, start);
    double distanceToEnd = SphericalDistanceLibrary.fastDistance(coord, end);

    if (cte < distanceToStart && cte < distanceToEnd) {
      double atd = alongTrackDistance(coord, cte);
      return atd / lineLength;
    } else {
      if (distanceToStart < distanceToEnd) {
        return 0;
      } else {
        return 1;
      }
    }
  }

  // in radians
  static double bearing(Coordinate c1, Coordinate c2) {
    double deltaLon = ((c2.x - c1.x) * Math.PI) / 180;
    double lat1Radians = (c1.y * Math.PI) / 180;
    double lat2Radians = (c2.y * Math.PI) / 180;
    double y = Math.sin(deltaLon) * Math.cos(lat2Radians);
    double x =
      Math.cos(lat1Radians) * Math.sin(lat2Radians) -
      Math.sin(lat1Radians) * Math.cos(lat2Radians) * Math.cos(deltaLon);
    return Math.atan2(y, x);
  }

  double crossTrackError(Coordinate coord) {
    double distanceFromStart = SphericalDistanceLibrary.fastDistance(start, coord);
    double bearingToCoord = bearing(start, coord);
    return (
      Math.asin(Math.sin(distanceFromStart / RADIUS) * Math.sin(bearingToCoord - bearingToEnd)) *
      RADIUS
    );
  }

  /**
   * This is called for every combination of stop and shape segment, so the along-track distances
   * are only computed in the branch that needs them.
   */
  double distance(Coordinate coord) {
    double cte = crossTrackError(coord);
    double distanceToStart = SphericalDistanceLibrary.fastDistance(coord, start);
    double distanceToEnd = SphericalDistanceLibrary.fastDistance(coord, end);

    if (distanceToStart < distanceToEnd) {
      //we might be behind the line start
      if (inverseAlongTrackDistance(coord, -cte) > lineLength) {
        //we are behind line start
        return distanceToStart;
      } else {
        //we are within line
        return Math.abs(cte);
      }
    } else {
      //we might be after line end
      if (alongTrackDistance(coord, cte) > lineLength) {
        //we are behind line end, so we that's the nearest point
        return distanceToEnd;
      } else {
        //we are within line
        return Math.abs(cte);
      }
    }
  }

  private double inverseAlongTrackDistance(Coordinate coord, double inverseCrossTrackError) {
    double distanceFromEnd = SphericalDistanceLibrary.fastDistance(end, coord);
    return (
      Math.acos(Math.cos(distanceFromEnd / RADIUS) / Math.cos(inverseCrossTrackError / RADIUS)) *
      RADIUS
    );
  }

  private double alongTrackDistance(Coordinate coord, double crossTrackError) {
    double distanceFromStart = SphericalDistanceLibrary.fastDistance(start, coord);
    return (
      Math.acos(Math.cos(distanceFromStart / RADIUS) / Math.cos(crossTrackError / RADIUS)) * RADIUS
    );
  }
}
