package org.opentripplanner.standalone.configure;

import dagger.Subcomponent;
import org.opentripplanner.standalone.api.HttpRequestScoped;
import org.opentripplanner.transit.configure.TransitModule;
import org.opentripplanner.transit.service.TransitService;

@HttpRequestScoped
@Subcomponent(modules = TransitModule.class)
public interface RequestScopedComponent {
  TransitService transitService();

  @Subcomponent.Builder
  interface Builder {
    RequestScopedComponent build();
  }
}
