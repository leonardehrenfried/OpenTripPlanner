package org.opentripplanner.ext.flex.template;

import java.util.Collection;
import org.jspecify.annotations.Nullable;
import org.opentripplanner.core.model.id.FeedScopedId;
import org.opentripplanner.ext.flex.FlexParameters;
import org.opentripplanner.ext.flex.FlexPathDurations;
import org.opentripplanner.ext.flex.flexpathcalculator.FlexPathCalculator;
import org.opentripplanner.ext.flex.path.FlexStreetPath;
import org.opentripplanner.ext.flex.trip.FlexTrip;
import org.opentripplanner.place.api.NearbyStop;
import org.opentripplanner.street.api.path.StreetPath;
import org.opentripplanner.street.api.path.StreetSearchResult;
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

  protected FlexPathDurations calculateFlexPathDurations(FlexStreetPath flexPath, StreetPath path) {
    int postFlexTime = (int) accessEgress.duration().toSeconds();
    int flexTimeInSeconds = flexPath.durationSeconds();
    int preFlexTime = (int) path.duration().toSeconds() - postFlexTime - flexTimeInSeconds;
    return new FlexPathDurations(
      preFlexTime,
      flexTimeInSeconds,
      postFlexTime,
      secondsFromStartOfTime
    );
  }

  protected FlexStreetPath getFlexPath(Vertex flexFromVertex, FeedScopedId transferStopId) {
    var flexPath = calculator.calculateFlexPath(
      flexFromVertex,
      accessEgress.vertex(),
      boardStopPosition,
      alightStopPosition
    );

    if (flexPath == null) {
      return null;
    }

    return new FlexStreetPath(
      flexFromVertex,
      accessEgress.vertex(),
      transferStopId,
      accessEgress.stopId(),
      trip,
      boardStopPosition,
      alightStopPosition,
      serviceDate,
      flexPath,
      flexParameters,
      accessEgress.result().startTime().minusSeconds(flexPath.durationSeconds),
      true
    );
  }

  protected StreetPath combinePaths(
    FlexStreetPath flexPath,
    @Nullable StreetSearchResult transferPath
  ) {
    return transferPath == null
      ? StreetPath.concat(flexPath, accessEgress.result())
      : StreetPath.concat(transferPath, flexPath, accessEgress.result());
  }
}
