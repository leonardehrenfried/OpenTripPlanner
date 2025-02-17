package org.opentripplanner.framework.geometry;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

public abstract class SphericalDistanceLibrary {

  public static final double RADIUS_OF_EARTH_IN_KM = 6371.01;
  public static final double RADIUS_OF_EARTH_IN_M = RADIUS_OF_EARTH_IN_KM * 1000;

  // Max admissible lat/lon delta for approximated distance computation
  public static final double MAX_LAT_DELTA_DEG = 4.0;
  public static final double MAX_LON_DELTA_DEG = 4.0;

  // 1 / Max over-estimation error of approximated distance,
  // for delta lat/lon in given range
  public static final double MAX_ERR_INV = 0.999462;

  /**
   * Returns the distance between two coordinates in meters.
   */
  public static double distance(Coordinate from, Coordinate to) {
    return distance(from.y, from.x, to.y, to.x);
  }

  /**
   * @see SphericalDistanceLibrary#fastDistance(double, double, double, double)
   */
  public static double fastDistance(Coordinate from, Coordinate to) {
    return fastDistance(from.y, from.x, to.y, to.x);
  }

  /**
   * Compute the length of a polyline
   *
   * @param lineString The polyline in (longitude, latitude degrees).
   * @return The length, in meters, of the linestring.
   */
  public static double length(LineString lineString) {
    double accumulatedMeters = 0;

    for (int i = 1; i < lineString.getNumPoints(); i++) {
      accumulatedMeters += distance(lineString.getCoordinateN(i - 1), lineString.getCoordinateN(i));
    }

    return accumulatedMeters;
  }

  /**
   * Returns the distance between two coordinates in meters.
   */
  public static double distance(double lat1, double lon1, double lat2, double lon2) {
    return distance(lat1, lon1, lat2, lon2, RADIUS_OF_EARTH_IN_M);
  }

  /**
   * Compute an (approximated) distance in meters between two points, with a known cos(lat).
   * Be careful, this is approximated and never checks for the validity of input cos(lat).
   */
  public static double fastDistance(double lat1, double lon1, double lat2, double lon2) {
    if (abs(lat1 - lat2) > MAX_LAT_DELTA_DEG || abs(lon1 - lon2) > MAX_LON_DELTA_DEG) {
      return distance(lat1, lon1, lat2, lon2, RADIUS_OF_EARTH_IN_M);
    }
    double dLat = toRadians(lat2 - lat1);
    double dLon = toRadians(lon2 - lon1) * cos(toRadians((lat1 + lat2) / 2));
    return RADIUS_OF_EARTH_IN_M * sqrt(dLat * dLat + dLon * dLon) * MAX_ERR_INV;
  }

  /**
   * @param distanceMeters Distance in meters.
   * @return The number of degree for the given distance. For degrees latitude, this is nearly
   * correct. For degrees longitude, this is an overestimate because meridians converge toward the
   * poles.
   */
  public static double metersToDegrees(double distanceMeters) {
    return 360 * distanceMeters / (2 * Math.PI * RADIUS_OF_EARTH_IN_M);
  }

  /**
   * @param distanceMeters Distance in meters.
   * @param latDeg         Latitude of center point, in degree.
   * @return The number of longitude degree for the given distance. This is a slight overestimate as
   * the number of degree of longitude for a given distance depends on the exact latitude.
   */
  public static double metersToLonDegrees(double distanceMeters, double latDeg) {
    double dLatDeg = 360 * distanceMeters / (2 * Math.PI * RADIUS_OF_EARTH_IN_M);
    /*
     * The computation below ensure that minCosLat is the minimum value of cos(lat) for lat in
     * the range [lat-dLat, lat+dLat].
     */
    double minCosLat;
    if (latDeg > 0) {
      minCosLat = cos(toRadians(latDeg + dLatDeg));
    } else {
      minCosLat = cos(toRadians(latDeg - dLatDeg));
    }
    return dLatDeg / minCosLat;
  }

  /**
   * Approximately move a coordinate a given number of meters. This will fail if crossing the anti-
   * meridian or any of the poles.
   */
  public static WgsCoordinate moveMeters(
    WgsCoordinate coordinate,
    double latMeters,
    double lonMeters
  ) {
    var degreesLat = metersToDegrees(latMeters);
    var degreesLon = metersToLonDegrees(lonMeters, coordinate.latitude());
    return coordinate.add(degreesLat, degreesLon);
  }

  private static double distance(double lat1, double lon1, double lat2, double lon2, double radius) {
    // http://en.wikipedia.org/wiki/Great-circle_distance
    lat1 = toRadians(lat1); // Theta-s
    lon1 = toRadians(lon1); // Lambda-s
    lat2 = toRadians(lat2); // Theta-f
    lon2 = toRadians(lon2); // Lambda-f

    double deltaLon = lon2 - lon1;

    double y = sqrt(
      p2(cos(lat2) * sin(deltaLon)) +
        p2(cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(deltaLon))
    );
    double x = sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(deltaLon);

    return radius * atan2(y, x);
  }

  private static double p2(double a) {
    return a * a;
  }
}
