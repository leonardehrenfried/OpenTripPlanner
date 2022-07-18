package org.opentripplanner.routing.algorithm.raptoradapter.transit.request;

import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.opentripplanner.common.GeojsonIoUrlGenerator;
import org.opentripplanner.common.geometry.GeometryUtils;
import org.opentripplanner.model.modes.AllowTransitModeFilter;
import org.opentripplanner.routing.algorithm.raptoradapter.transit.TripPatternForDate;
import org.opentripplanner.routing.algorithm.raptoradapter.transit.TripPatternWithRaptorStopIndexes;
import org.opentripplanner.routing.api.request.RoutingRequest;
import org.opentripplanner.routing.api.request.StreetMode;
import org.opentripplanner.routing.api.request.WheelchairAccessibilityRequest;
import org.opentripplanner.routing.trippattern.TripTimes;
import org.opentripplanner.transit.model.basic.WheelchairAccessibility;
import org.opentripplanner.transit.model.framework.FeedScopedId;
import org.opentripplanner.transit.model.network.BikeAccess;
import org.opentripplanner.transit.model.network.MainAndSubMode;
import org.opentripplanner.transit.model.timetable.Trip;
import org.opentripplanner.transit.service.TransitModelIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoutingRequestTransitDataProviderFilter implements TransitDataProviderFilter {

  private static Logger LOG = LoggerFactory.getLogger(
    RoutingRequestTransitDataProviderFilter.class
  );
  private final boolean requireBikesAllowed;

  private final WheelchairAccessibilityRequest wheelchairAccessibility;

  private final boolean includePlannedCancellations;

  private final AllowTransitModeFilter transitModeFilter;

  private final Set<FeedScopedId> bannedRoutes;

  private final Set<FeedScopedId> bannedTrips;
  private final Set<FeedScopedId> routesInsideEnvelope;

  public RoutingRequestTransitDataProviderFilter(
    boolean requireBikesAllowed,
    WheelchairAccessibilityRequest accessibility,
    boolean includePlannedCancellations,
    Collection<MainAndSubMode> allowedTransitModes,
    Set<FeedScopedId> bannedRoutes,
    Set<FeedScopedId> bannedTrips,
    Set<FeedScopedId> routesInsideEnvelope
  ) {
    this.requireBikesAllowed = requireBikesAllowed;
    this.wheelchairAccessibility = accessibility;
    this.includePlannedCancellations = includePlannedCancellations;
    this.bannedRoutes = bannedRoutes;
    this.bannedTrips = bannedTrips;
    this.transitModeFilter = AllowTransitModeFilter.of(allowedTransitModes);
    this.routesInsideEnvelope = routesInsideEnvelope;
  }

  public RoutingRequestTransitDataProviderFilter(
    RoutingRequest request,
    TransitModelIndex transitModelIndex
  ) {
    this(
      request.modes.transferMode == StreetMode.BIKE,
      request.wheelchairAccessibility,
      request.includePlannedCancellations,
      request.modes.transitModes,
      request.getBannedRoutes(transitModelIndex.getAllRoutes()),
      request.bannedTrips,
      getRoutesInsideEnvelope(request, transitModelIndex)
    );
  }

  private static Set<FeedScopedId> getRoutesInsideEnvelope(
    RoutingRequest req,
    TransitModelIndex transitModelIndex
  ) {
    var env = new Envelope();
    env.expandToInclude(req.from.getCoordinate());
    env.expandToInclude(req.to.getCoordinate());

    // for smaller distances we want a relatively large buffer, for larger distance the buffer can be smaller
    // this what this formula is for
    var distance = req.from.getCoordinate().distance(req.to.getCoordinate());
    var x = 0.4 / distance;
    var y = distance / 2;
    env.expandBy(Math.min(x, y));

    var filtered = new HashSet<FeedScopedId>(transitModelIndex.getRouteSpatialIndex().query(env));
    // we need to actually have a sizable reduction in routes otherwise we just spend all of our
    // time figuring out if a route is inside the envelope or not
    if (LOG.isDebugEnabled()) {
      var topLeft = new Coordinate(env.getMaxX(), env.getMaxY());
      var topRight = new Coordinate(env.getMinX(), env.getMaxY());
      var bottomLeft = new Coordinate(env.getMinX(), env.getMinY());
      var bottomRight = new Coordinate(env.getMaxX(), env.getMinY());
      var p = GeometryUtils
        .getGeometryFactory()
        .createPolygon(new Coordinate[] { topLeft, topRight, bottomLeft, bottomRight, topLeft });
      LOG.info(
        "Limiting RAPTOR search to routes that intersect envelope {}",
        GeojsonIoUrlGenerator.geometryUrl(p)
      );
    }
    return filtered;
  }

  public static BikeAccess bikeAccessForTrip(Trip trip) {
    if (trip.getBikesAllowed() != BikeAccess.UNKNOWN) {
      return trip.getBikesAllowed();
    }

    return trip.getRoute().getBikesAllowed();
  }

  @Override
  public boolean tripPatternPredicate(TripPatternForDate tripPatternForDate) {
    FeedScopedId routeId = tripPatternForDate.getTripPattern().getPattern().getRoute().getId();
    if (Objects.isNull(routesInsideEnvelope)) {
      return !bannedRoutes.contains(routeId);
    } else {
      return routesInsideEnvelope.contains(routeId) && !bannedRoutes.contains(routeId);
    }
  }

  @Override
  public boolean tripTimesPredicate(TripTimes tripTimes) {
    final Trip trip = tripTimes.getTrip();
    if (!transitModeFilter.allows(trip.getMode(), trip.getNetexSubMode())) {
      return false;
    }

    if (bannedTrips.contains(trip.getId())) {
      return false;
    }

    if (requireBikesAllowed) {
      if (bikeAccessForTrip(trip) != BikeAccess.ALLOWED) {
        return false;
      }
    }

    if (wheelchairAccessibility.enabled()) {
      if (
        wheelchairAccessibility.trip().onlyConsiderAccessible() &&
        trip.getWheelchairBoarding() != WheelchairAccessibility.POSSIBLE
      ) {
        return false;
      }
    }

    if (!includePlannedCancellations) {
      //noinspection RedundantIfStatement
      if (trip.getNetexAlteration().isCanceledOrReplaced()) {
        return false;
      }
    }

    return true;
  }

  @Override
  public BitSet filterAvailableStops(
    TripPatternWithRaptorStopIndexes tripPattern,
    BitSet boardingPossible
  ) {
    // if the user wants wheelchair-accessible routes and the configuration requires us to only
    // consider those stops which have the correct accessibility values then use only this for
    // checking whether to board/alight
    if (
      wheelchairAccessibility.enabled() && wheelchairAccessibility.stop().onlyConsiderAccessible()
    ) {
      var copy = (BitSet) boardingPossible.clone();
      // Use the and bitwise operator to add false flag to all stops that are not accessible by wheelchair
      copy.and(tripPattern.getWheelchairAccessible());

      return copy;
    }
    return boardingPossible;
  }
}
