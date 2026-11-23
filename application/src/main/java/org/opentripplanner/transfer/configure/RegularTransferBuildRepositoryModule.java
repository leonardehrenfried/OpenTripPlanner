package org.opentripplanner.transfer.configure;

import dagger.Module;
import dagger.Provides;
import jakarta.inject.Singleton;
import org.opentripplanner.transfer.regular.model.StreetTransferPath;
import org.opentripplanner.transit.transfer.regular.RegularTransferBuildRepository;
import org.opentripplanner.transit.transfer.regular.configure.RegularTransferFactory;

@Module
public class RegularTransferBuildRepositoryModule {

  @Provides
  @Singleton
  public RegularTransferBuildRepository<StreetTransferPath> provideRegularTransferBuildRepository() {
    return RegularTransferFactory.createBuildRepository();
  }
}
