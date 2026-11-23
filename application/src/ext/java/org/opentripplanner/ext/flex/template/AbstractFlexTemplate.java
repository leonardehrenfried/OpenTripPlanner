package org.opentripplanner.ext.flex.template;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;
import org.opentripplanner.core.model.id.FeedScopedId;
import org.opentripplanner.ext.flex.FlexAccessEgress;
import org.opentripplanner.ext.flex.FlexParameters;
import org.opentripplanner.ext.flex.FlexPathDurations;
import org.opentripplanner.ext.flex.flexpathcalculator.FlexPathCalculator;
import org.opentripplanner.ext.flex.path.FlexStreetPath;
import org.opentripplanner.ext.flex.trip.FlexTrip;
import org.opentripplanner.place.api.NearbyStop;
import org.opentripplanner.street.model.path.DefaultStreetSearchResult;
import org.opentripplanner.street.model.path.StreetPath;
import org.opentripplanner.street.model.path.StreetSearchResult;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.transfer.regular.model.PathTransfer;
import org.opentripplanner.transit.model.site.RegularStop;
import org.opentripplanner.transit.model.site.StopLocation;
import org.opentripplanner.utils.tostring.ToStringBuilder;

/**
 * A container for a few pieces of information that can be used to calculate flex accesses,
 * egresses, direct flex itineraries or polylines.
 * <p>
 * Please also see Flex.svg for an illustration of how the flex concepts relate to each other.
 */
abstract class AbstractFlexTemplate {

  /**
   * We do not want extremely short flex trips, they will normally be dominated in the routing
   * later. We set an absolute min duration to 10 seconds (167m with 60 km/h).
   */
  private static final int MIN_FLEX_TRIP_DURATION_SECONDS = 10;

  // TODO - This is confusing, and not following OO principles. The from/to stop
  //      - changes type for access/egress, move them down into child class.
  //      - this apply to transferStop as well.
  protected final NearbyStop accessEgress;
  protected final FlexTrip<?, ?> trip;
  protected final int boardStopPosition;
  protected final int alightStopPosition;
  protected final StopLocation transferStop;
  protected final int secondsFromStartOfTime;
  protected final LocalDate serviceDate;
  protected final int requestedBookingTime;
  protected final FlexPathCalculator calculator;
  protected final FlexParameters flexParameters;

  /**
   * @param trip               The FlexTrip used for this template
   * @param accessEgress       Path from origin/destination to the point of boarding/alighting for
   *                           this flex trip
   * @param transferStop       The stop location where this FlexTrip transfers to another transit
   *                           service.
   * @param boardStopPosition  The stop-board-position in the trip pattern
   * @param alightStopPosition The stop-alight-position in the trip pattern
   * @param date               The service date of this FlexTrip
   * @param calculator         Calculates the path and duration of the FlexTrip
   * @param flexParameters     Flex configuration parameters (maxTransferDuration, etc.)
   */
  AbstractFlexTemplate(
    FlexTrip<?, ?> trip,
    NearbyStop accessEgress,
    StopLocation transferStop,
    int boardStopPosition,
    int alightStopPosition,
    FlexServiceDate date,
    FlexPathCalculator calculator,
    FlexParameters flexParameters
  ) {
    this.accessEgress = accessEgress;
    this.trip = trip;
    this.boardStopPosition = boardStopPosition;
    this.alightStopPosition = alightStopPosition;
    this.transferStop = transferStop;
    this.secondsFromStartOfTime = date.secondsFromStartOfTime();
    this.serviceDate = date.serviceDate();
    this.requestedBookingTime = date.requestedBookingTime();
    this.calculator = calculator;
    this.flexParameters = flexParameters;
  }

  FeedScopedId getTransferStopId() {
    return transferStop.getId();
  }

  FeedScopedId getAccessEgressStopId() {
    return accessEgress.stopId();
  }

  /**
   * This method is very much the hot code path in the flex access/egress search, so any
   * optimization here will lead to noticeable speedups.
   */
  Stream<FlexAccessEgress> createFlexAccessEgressStream(FlexAccessEgressCallbackAdapter callback) {
    if (transferStop instanceof RegularStop stop) {
      var flexVertex = callback.getStopVertex(stop.getId());
      return Stream.of(createFlexAccessEgress(null, flexVertex, stop)).filter(Objects::nonNull);
    }
    // transferStop is Location Area/Line
    else {
      double maxDistanceMeters =
        flexParameters.maxTransferDuration().getSeconds() * callback.walkSpeed();

      return getTransfersFromTransferStop(callback)
        .stream()
        .filter(pathTransfer -> pathTransfer.getDistanceMeters() <= maxDistanceMeters)
        .filter(transfer -> getFinalStop(transfer) != null)
        .map(transfer -> {
          var transferPath = transfer.getPath();
          var flexVertex = getFlexVertex(transferPath);
          var finalStop = getFinalStop(transfer);
          return createFlexAccessEgress(transferPath, flexVertex, finalStop);
        })
        .filter(Objects::nonNull);
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.of(AbstractFlexTemplate.class)
      .addObj("accessEgress", accessEgress)
      .addObj("trip", trip)
      .addNum("boardStopPosition", boardStopPosition)
      .addNum("alightStopPosition", alightStopPosition)
      .addObj("transferStop", transferStop)
      .addServiceTime("secondsFromStartOfTime", secondsFromStartOfTime)
      .addDate("serviceDate", serviceDate)
      .addObj("calculator", calculator)
      .addDuration("maxTransferDuration", flexParameters.maxTransferDuration())
      .toString();
  }

  /**
   * Get the {@Link Stop} where the connection to the scheduled transit network is made.
   */
  protected abstract RegularStop getFinalStop(PathTransfer transfer);

  /**
   * Get the transfers to/from stops in the scheduled transit network from the beginning/end of the
   * flex ride for the access/egress.
   */
  protected abstract Collection<PathTransfer> getTransfersFromTransferStop(
    FlexAccessEgressCallbackAdapter callback
  );

  /**
   * Get the {@Link Vertex} where the flex ride ends/begins for the access/egress.
   */
  protected abstract Vertex getFlexVertex(StreetPath transferPath);

  /**
   * Break down the time spent on flex ride/path in access, trip and egress.
   */
  protected abstract FlexPathDurations calculateFlexPathDurations(
    FlexStreetPath flexPath,
    StreetPath path
  );

  /**
   * Get the path of the flex ride, or null if the trip can't be used between the vertices.
   */
  @Nullable
  protected abstract FlexStreetPath getFlexPath(Vertex flexVertex, FeedScopedId transferStopId);

  /**
   * Combine the path to/from the access/egress stop, the flex ride and the transfer path into a
   * single path in chronological order.
   */
  protected abstract StreetPath combinePaths(
    FlexStreetPath flexPath,
    @Nullable StreetSearchResult transferPath
  );

  /**
   * The result of the street search which found the access/egress stop, so the search can be
   * continued after the flex ride.
   */
  protected static DefaultStreetSearchResult streetSearchResult(NearbyStop nearbyStop) {
    if (nearbyStop.result() instanceof DefaultStreetSearchResult result) {
      return result;
    }
    throw new IllegalStateException(
      "The flex access/egress stop %s was not found by a street search.".formatted(
        nearbyStop.stopId()
      )
    );
  }

  @Nullable
  private FlexAccessEgress createFlexAccessEgress(
    @Nullable StreetPath transferPath,
    Vertex flexVertex,
    RegularStop stop
  ) {
    var flexPath = getFlexPath(flexVertex, transferStop.getId());

    // Drop non-routable and very short(<10s) trips
    if (flexPath == null || flexPath.durationSeconds() < MIN_FLEX_TRIP_DURATION_SECONDS) {
      return null;
    }

    StreetSearchResult traversedTransferPath = null;
    if (transferPath != null) {
      // The street search continues along the transfer path after the flex ride
      traversedTransferPath = streetSearchResult(accessEgress)
        .continueSearchAfter(flexPath.duration(), transferPath)
        .orElse(null);
      if (traversedTransferPath == null) {
        return null;
      }
    }

    var finalPath = combinePaths(flexPath, traversedTransferPath);
    return new FlexAccessEgress(
      stop,
      calculateFlexPathDurations(flexPath, finalPath),
      boardStopPosition,
      alightStopPosition,
      trip,
      finalPath,
      transferPath == null || transferPath.isEmpty(),
      requestedBookingTime
    );
  }
}
