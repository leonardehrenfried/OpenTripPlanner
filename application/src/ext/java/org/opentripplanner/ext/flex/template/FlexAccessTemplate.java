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

class FlexAccessTemplate extends AbstractFlexTemplate {

  FlexAccessTemplate(
    FlexTrip<?, ?> trip,
    NearbyStop boardStop,
    int boardStopPosition,
    StopLocation alightStop,
    int alightStopPosition,
    FlexServiceDate date,
    FlexPathCalculator calculator,
    FlexParameters flexParameters
  ) {
    super(
      trip,
      boardStop,
      alightStop,
      boardStopPosition,
      alightStopPosition,
      date,
      calculator,
      flexParameters
    );
  }

  protected RegularStop getFinalStop(PathTransfer transfer) {
    return transfer.to instanceof RegularStop ? (RegularStop) transfer.to : null;
  }

  protected Collection<PathTransfer> getTransfersFromTransferStop(
    FlexAccessEgressCallbackAdapter callback
  ) {
    return callback.getTransfersFromStop(transferStop);
  }

  protected Vertex getFlexVertex(StreetPath transferPath) {
    return transferPath.fromVertex();
  }

  protected FlexPathDurations calculateFlexPathDurations(FlexTripEdge flexEdge, StreetPath path) {
    int preFlexTime = (int) accessEgress.path().duration().toSeconds();
    int edgeTimeInSeconds = flexEdge.getTimeInSeconds();
    int postFlexTime = (int) path.duration().toSeconds() - preFlexTime - edgeTimeInSeconds;
    return new FlexPathDurations(
      preFlexTime,
      edgeTimeInSeconds,
      postFlexTime,
      secondsFromStartOfTime
    );
  }

  protected FlexTripEdge getFlexEdge(Vertex flexToVertex, FeedScopedId transferStopId) {
    var flexPath = calculator.calculateFlexPath(
      accessEgress.vertex(),
      flexToVertex,
      boardStopPosition,
      alightStopPosition
    );

    if (flexPath == null) {
      return null;
    }

    return new FlexTripEdge(
      accessEgress.vertex(),
      flexToVertex,
      accessEgress.stopId(),
      transferStopId,
      trip,
      boardStopPosition,
      alightStopPosition,
      serviceDate,
      flexPath,
      flexParameters
    );
  }
}
