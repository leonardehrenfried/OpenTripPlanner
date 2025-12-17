package org.opentripplanner.standalone.config.configure;

import dagger.Module;
import dagger.Provides;
import org.opentripplanner.transit.model.framework.Deduplicator;
import org.opentripplanner.transit.model.framework.DeduplicatorService;

@Module
public class DeduplicatorServiceModule {

  @Provides
  public static DeduplicatorService provideDeduplicatorService() {
    return new Deduplicator();
  }
}
