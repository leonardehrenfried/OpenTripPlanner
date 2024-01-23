package org.opentripplanner.standalone.configure;

import dagger.Module;
import dagger.Provides;
import dagger.internal.Provider;
import io.micrometer.core.instrument.Metrics;
import java.util.List;
import javax.annotation.Nullable;
import org.opentripplanner.astar.spi.TraverseVisitor;
import org.opentripplanner.ext.emissions.EmissionsService;
import org.opentripplanner.ext.interactivelauncher.api.LauncherRequestDecorator;
import org.opentripplanner.ext.ridehailing.RideHailingService;
import org.opentripplanner.ext.stopconsolidation.StopConsolidationService;
import org.opentripplanner.raptor.configure.RaptorConfig;
import org.opentripplanner.routing.algorithm.raptoradapter.transit.TripSchedule;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.service.realtimevehicles.RealtimeVehicleService;
import org.opentripplanner.service.vehiclerental.VehicleRentalService;
import org.opentripplanner.service.worldenvelope.WorldEnvelopeService;
import org.opentripplanner.standalone.api.OtpServerRequestContext;
import org.opentripplanner.standalone.config.RouterConfig;
import org.opentripplanner.standalone.server.DefaultServerRequestContext;
import org.opentripplanner.visualizer.GraphVisualizer;

@Module(subcomponents = RequestScopedComponent.class)
public class ConstructApplicationModule {

  @Provides
  OtpServerRequestContext providesServerContext(
    RouterConfig routerConfig,
    RaptorConfig<TripSchedule> raptorConfig,
    Graph graph,
    WorldEnvelopeService worldEnvelopeService,
    RealtimeVehicleService realtimeVehicleService,
    VehicleRentalService vehicleRentalService,
    List<RideHailingService> rideHailingServices,
    @Nullable StopConsolidationService stopConsolidationService,
    @Nullable TraverseVisitor<?, ?> traverseVisitor,
    EmissionsService emissionsService,
    LauncherRequestDecorator launcherRequestDecorator,
    Provider<RequestScopedComponent.Builder> requestComponentProvider
  ) {
    var defaultRequest = launcherRequestDecorator.intercept(routerConfig.routingRequestDefaults());

    return DefaultServerRequestContext.create(
      routerConfig.transitTuningConfig(),
      defaultRequest,
      raptorConfig,
      graph,
      requestComponentProvider.get().build().transitService(),
      Metrics.globalRegistry,
      routerConfig.vectorTileLayers(),
      worldEnvelopeService,
      realtimeVehicleService,
      vehicleRentalService,
      emissionsService,
      routerConfig.flexConfig(),
      rideHailingServices,
      stopConsolidationService,
      traverseVisitor
    );
  }

  @Provides
  @Nullable
  TraverseVisitor<?, ?> traverseVisitor(@Nullable GraphVisualizer graphVisualizer) {
    return graphVisualizer == null ? null : graphVisualizer.traverseVisitor;
  }
}
