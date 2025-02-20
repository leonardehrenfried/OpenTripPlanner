package org.opentripplanner.transit.configure;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import jakarta.inject.Singleton;
import java.time.LocalDate;
import org.opentripplanner.model.TimetableSnapshot;
import org.opentripplanner.routing.algorithm.raptoradapter.transit.RaptorTransitData;
import org.opentripplanner.routing.algorithm.raptoradapter.transit.mappers.RealTimeRaptorTransitDataUpdater;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.impl.DelegatingTransitAlertServiceImpl;
import org.opentripplanner.routing.services.TransitAlertService;
import org.opentripplanner.service.realtimevehicles.RealtimeVehicleRepository;
import org.opentripplanner.service.vehicleparking.VehicleParkingRepository;
import org.opentripplanner.service.vehiclerental.VehicleRentalRepository;
import org.opentripplanner.standalone.api.HttpRequestScoped;
import org.opentripplanner.standalone.config.ConfigModel;
import org.opentripplanner.standalone.config.RouterConfig;
import org.opentripplanner.transit.service.DefaultTransitService;
import org.opentripplanner.transit.service.TimetableRepository;
import org.opentripplanner.transit.service.TransitService;
import org.opentripplanner.updater.GraphUpdaterManager;
import org.opentripplanner.updater.GraphUpdaterStatus;
import org.opentripplanner.updater.UpdatersParameters;
import org.opentripplanner.updater.configure.UpdaterConfigurator;
import org.opentripplanner.updater.trip.TimetableSnapshotManager;

@Module
public abstract class TransitModule {

  @Binds
  @HttpRequestScoped
  abstract TransitService bind(DefaultTransitService service);

  @Provides
  @Singleton
  public static TimetableSnapshotManager timetableSnapshotManager(
    RealTimeRaptorTransitDataUpdater realtimeRaptorTransitDataUpdater,
    ConfigModel config,
    TimetableRepository timetableRepository
  ) {
    return new TimetableSnapshotManager(
      realtimeRaptorTransitDataUpdater,
      config.routerConfig().updaterConfig().timetableSnapshotParameters(),
      () -> LocalDate.now(timetableRepository.getTimeZone())
    );
  }

  /**
   * Create a single instance of the transit layer updater which holds the incremental caches for
   * the updates that need to applied to the {@link RaptorTransitData}.
   */
  @Provides
  @Singleton
  public static RealTimeRaptorTransitDataUpdater realtimeRaptorTransitDataUpdater(
    TimetableRepository timetableRepository
  ) {
    return new RealTimeRaptorTransitDataUpdater(timetableRepository);
  }

  /**
   * Provides the currently published, immutable {@link TimetableSnapshot}.
   */
  @Provides
  public static TimetableSnapshot timetableSnapshot(TimetableSnapshotManager manager) {
    return manager.getTimetableSnapshot();
  }

  @Provides
  @Singleton
  public static TransitAlertService transitAlertService(GraphUpdaterManager manager) {
    return new DelegatingTransitAlertServiceImpl(manager);
  }

  @Provides
  @Singleton
  public static GraphUpdaterManager updaterManager(
    Graph graph,
    RealtimeVehicleRepository realtimeVehicleRepository,
    VehicleRentalRepository vehicleRentalRepository,
    VehicleParkingRepository vehicleParkingRepository,
    TimetableRepository timetableRepository,
    TimetableSnapshotManager timetableSnapshotManager,
    RouterConfig routerConfig
  ) {
    return UpdaterConfigurator.configure(
      graph,
      realtimeVehicleRepository,
      vehicleRentalRepository,
      vehicleParkingRepository,
      timetableRepository,
      timetableSnapshotManager,
      routerConfig.updaterConfig()
    );
  }

  @Binds
  @Singleton
  abstract GraphUpdaterStatus updaterStatus(GraphUpdaterManager manager);
}
