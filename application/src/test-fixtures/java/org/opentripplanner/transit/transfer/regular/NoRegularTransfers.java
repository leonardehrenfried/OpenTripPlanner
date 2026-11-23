package org.opentripplanner.transit.transfer.regular;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.opentripplanner.raptor.data.transfers.regular.RaptorRegularTransferService;
import org.opentripplanner.raptor.spi.RaptorTransfer;
import org.opentripplanner.routing.api.request.RouteRequest;
import org.opentripplanner.transfer.regular.model.StreetTransferPath;
import org.opentripplanner.transit.transfer.regular.api.RegularTransfersService;
import org.opentripplanner.transit.transfer.regular.api.report.RegularTransferCacheMetrics;
import org.opentripplanner.transit.transfer.regular.api.report.RegularTransferCacheReport;

/**
 * A regular-transfer snapshot without any transfers, for tests that route without regular
 * transfers. Use {@code TestServerContext.createRegularTransfers} to route with the transfers
 * generated from the test graph.
 */
public final class NoRegularTransfers {

  private NoRegularTransfers() {}

  public static RegularTransferRepositorySnapshot<StreetTransferPath, RouteRequest> snapshot() {
    return SNAPSHOT;
  }

  public static RegularTransfersService<StreetTransferPath> transfers() {
    return TRANSFERS;
  }

  public static RaptorRegularTransferService service() {
    return SERVICE;
  }

  private static final RaptorRegularTransferService SERVICE = new RaptorRegularTransferService() {
    @Override
    public Iterator<? extends RaptorTransfer> getTransfersFromStop(int fromStop) {
      return Collections.emptyIterator();
    }

    @Override
    public Iterator<? extends RaptorTransfer> getTransfersToStop(int toStop) {
      return Collections.emptyIterator();
    }
  };

  private static final RegularTransfersService<StreetTransferPath> TRANSFERS =
    new RegularTransfersService<>() {
      @Override
      public RaptorRegularTransferService raptorTransfers() {
        return SERVICE;
      }

      @Nullable
      @Override
      public StreetTransferPath findPath(int fromStop, int toStop) {
        return null;
      }

      @Override
      public Collection<StreetTransferPath> findPathsFrom(int fromStop) {
        return List.of();
      }
    };

  private static final RegularTransferRepositorySnapshot<
    StreetTransferPath,
    RouteRequest
  > SNAPSHOT = new RegularTransferRepositorySnapshot<>() {
    @Override
    public RegularTransfersService<StreetTransferPath> forRequest(RouteRequest request) {
      return TRANSFERS;
    }

    @Override
    public void warmUp() {}

    @Override
    public RegularTransferCacheReport cacheReport() {
      return RegularTransferCacheReport.DISABLED;
    }

    @Override
    public RegularTransferCacheMetrics cacheMetrics() {
      return new RegularTransferCacheMetrics() {
        @Override
        public long size() {
          return 0;
        }

        @Override
        public long hitCount() {
          return 0;
        }

        @Override
        public long missCount() {
          return 0;
        }

        @Override
        public long putCount() {
          return 0;
        }

        @Override
        public long evictionCount() {
          return 0;
        }

        @Override
        public long loadFailureCount() {
          return 0;
        }

        @Override
        public long totalLoadTimeNanos() {
          return 0;
        }
      };
    }
  };
}
