package org.opentripplanner.transit.transfer.regular;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import org.opentripplanner.raptor.data.transfers.regular.RaptorRegularTransferService;
import org.opentripplanner.routing.api.request.RouteRequest;
import org.opentripplanner.transfer.regular.model.StreetTransferPath;
import org.opentripplanner.transit.model.site.StopLocation;
import org.opentripplanner.transit.transfer.regular.api.RegularTransfersService;
import org.opentripplanner.transit.transfer.regular.api.report.RegularTransferCacheMetrics;
import org.opentripplanner.transit.transfer.regular.api.report.RegularTransferCacheReport;

/**
 * A regular-transfer snapshot with a fixed set of transfer paths, for tests that only recover the
 * path of a transfer. The same paths are returned for every request, and there are no Raptor
 * transfers. Use {@link NoRegularTransfers} for a snapshot without any transfers.
 */
public final class FixedRegularTransfers {

  private final Map<Key, StreetTransferPath> paths = new HashMap<>();

  private FixedRegularTransfers() {}

  public static FixedRegularTransfers of() {
    return new FixedRegularTransfers();
  }

  /** Add the path of the transfer from {@code from} to {@code to}. */
  public FixedRegularTransfers add(StopLocation from, StopLocation to, StreetTransferPath path) {
    paths.put(new Key(from.getIndex(), to.getIndex()), path);
    return this;
  }

  public RegularTransferRepositorySnapshot<StreetTransferPath, RouteRequest> snapshot() {
    var paths = Map.copyOf(this.paths);
    var transfers = new RegularTransfersService<StreetTransferPath>() {
      @Override
      public RaptorRegularTransferService raptorTransfers() {
        return NoRegularTransfers.service();
      }

      @Nullable
      @Override
      public StreetTransferPath findPath(int fromStop, int toStop) {
        return paths.get(new Key(fromStop, toStop));
      }

      @Override
      public Collection<StreetTransferPath> findPathsFrom(int fromStop) {
        return paths
          .entrySet()
          .stream()
          .filter(e -> e.getKey().fromStop() == fromStop)
          .map(Map.Entry::getValue)
          .toList();
      }
    };
    var noTransfers = NoRegularTransfers.snapshot();

    return new RegularTransferRepositorySnapshot<>() {
      @Override
      public RegularTransfersService<StreetTransferPath> forRequest(RouteRequest request) {
        return transfers;
      }

      @Override
      public void warmUp() {}

      @Override
      public RegularTransferCacheReport cacheReport() {
        return noTransfers.cacheReport();
      }

      @Override
      public RegularTransferCacheMetrics cacheMetrics() {
        return noTransfers.cacheMetrics();
      }
    };
  }

  private record Key(int fromStop, int toStop) {}
}
