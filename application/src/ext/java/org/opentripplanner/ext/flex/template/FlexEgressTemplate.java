package org.opentripplanner.ext.flex.template;

import java.util.Collection;
import org.opentripplanner.core.model.id.FeedScopedId;
import org.opentripplanner.ext.flex.FlexParameters;
import org.opentripplanner.ext.flex.FlexPathDurations;
import org.opentripplanner.ext.flex.edgetype.FlexTripEdge;
import org.opentripplanner.ext.flex.flexpathcalculator.FlexPathCalculator;
import org.opentripplanner.ext.flex.trip.FlexTrip;
import org.opentripplanner.place.api.NearbyStop;
import org.opentripplanner.street.model.path.StreetPath;
import org.opentripplanner.street.model.vertex.Vertex;
import org.opentripplanner.transfer.regular.model.PathTransfer;
import org.opentripplanner.transit.model.site.RegularStop;
import org.opentripplanner.transit.model.site.StopLocation;

class FlexEgressTemplate extends AbstractFlexTemplate {

  FlexEgressTemplate(
    FlexTrip<?, ?> trip,
    StopLocation boardStop,
    int boardStopPosition,
    NearbyStop alightStop,
    int alightStopPosition,
    FlexServiceDate date,
    FlexPathCalculator calculator,
    FlexParameters flexParameters
  ) {
    super(
      trip,
      alightStop,
      boardStop,
      boardStopPosition,
      alightStopPosition,
      date,
      calculator,
      flexParameters
    );
  }

  protected RegularStop getFinalStop(PathTransfer transfer) {
    return transfer.from instanceof RegularStop regularStop ? regularStop : null;
  }

  protected Collection<PathTransfer> getTransfersFromTransferStop(
    FlexAccessEgressCallbackAdapter callback
  ) {
    return callback.getTransfersToStop(transferStop);
  }

  protected Vertex getFlexVertex(StreetPath transferPath) {
    return transferPath.toVertex();
  }

  protected FlexPathDurations calculateFlexPathDurations(FlexTripEdge flexEdge, StreetPath path) {
    int postFlexTime = (int) accessEgress.path().duration().toSeconds();
    int edgeTimeInSeconds = flexEdge.getTimeInSeconds();
    int preFlexTime = (int) path.duration().toSeconds() - postFlexTime - edgeTimeInSeconds;
    return new FlexPathDurations(
      preFlexTime,
      edgeTimeInSeconds,
      postFlexTime,
      secondsFromStartOfTime
    );
  }

  protected FlexTripEdge getFlexEdge(Vertex flexFromVertex, FeedScopedId transferStopId) {
    var flexPath = calculator.calculateFlexPath(
      flexFromVertex,
      accessEgress.vertex(),
      boardStopPosition,
      alightStopPosition
    );

    if (flexPath == null) {
      return null;
    }

    return new FlexTripEdge(
      flexFromVertex,
      accessEgress.vertex(),
      transferStopId,
      accessEgress.stopId(),
      trip,
      boardStopPosition,
      alightStopPosition,
      serviceDate,
      flexPath,
      flexParameters
    );
  }
}
