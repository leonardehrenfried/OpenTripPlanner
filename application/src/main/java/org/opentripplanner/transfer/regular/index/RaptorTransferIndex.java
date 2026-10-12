package org.opentripplanner.transfer.regular.index;

import static java.util.stream.Collectors.toMap;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import org.opentripplanner.framework.application.OTPFeature;
import org.opentripplanner.raptor.spi.RaptorTransfer;
import org.opentripplanner.street.search.request.StreetSearchRequest;
import org.opentripplanner.transfer.regular.model.DefaultRaptorTransfer;
import org.opentripplanner.transfer.regular.model.PathTransfer;

public interface RaptorTransferIndex {
  /**
   * Create an index for a route request configured in router-config.json
   */
  static RaptorTransferIndex createInitialSetup(
    List<List<PathTransfer>> transfersByStopIndex,
    StreetSearchRequest request
  ) {
    // We always want to parallelize the generation when OTP is starting up.
    return new PreCachedRaptorTransferIndex(transfersByStopIndex, atEpoch(request), true);
  }

  /**
   * Create an index for a route request originated from the client
   */
  static RaptorTransferIndex createRequestScope(
    List<List<PathTransfer>> transfersByStopIndex,
    StreetSearchRequest request
  ) {
    var epochRequest = atEpoch(request);
    return OTPFeature.OnDemandRaptorTransfer.isOn()
      ? new OnDemandRaptorTransferIndex(transfersByStopIndex, epochRequest)
      : new PreCachedRaptorTransferIndex(
          transfersByStopIndex,
          epochRequest,
          OTPFeature.ParallelRouting.isOn()
        );
  }

  /**
   * The transfers do not depend on the time of the search, so they are always traversed starting
   * at the epoch. This keeps the result independent of the request which created a cached index.
   */
  private static StreetSearchRequest atEpoch(StreetSearchRequest request) {
    return StreetSearchRequest.copyOf(request).withStartTime(Instant.EPOCH).build();
  }

  static Collection<DefaultRaptorTransfer> getRaptorTransfers(
    StreetSearchRequest request,
    List<PathTransfer> transfers
  ) {
    var mode = request.mode();
    // The transfers are filtered so that there is only one possible directional transfer
    // for a stop pair.
    return transfers
      .stream()
      .filter(transfer -> transfer.allowsMode(mode))
      .flatMap(s -> s.asRaptorTransfer(request).stream())
      .collect(toMap(RaptorTransfer::stop, Function.identity(), (a, b) -> a.c1() < b.c1() ? a : b))
      .values();
  }

  Collection<DefaultRaptorTransfer> getForwardTransfers(int stopIndex);

  Collection<DefaultRaptorTransfer> getReversedTransfers(int stopIndex);
}
