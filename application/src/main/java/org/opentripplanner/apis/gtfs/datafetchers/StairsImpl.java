package org.opentripplanner.apis.gtfs.datafetchers;

import static org.opentripplanner.framework.graphql.GraphQLUtils.*;

import graphql.schema.DataFetcher;
import org.opentripplanner.apis.gtfs.generated.GraphQLDataFetchers;
import org.opentripplanner.model.plan.Stairs;

public class StairsImpl implements GraphQLDataFetchers.GraphQLStairs {

  @Override
  public DataFetcher<String> name() {
    return environment -> {
      Stairs stairs = environment.getSource();
      return stairs.name().map(name -> getTranslation(name, environment)).orElse(null);
    };
  }
}
