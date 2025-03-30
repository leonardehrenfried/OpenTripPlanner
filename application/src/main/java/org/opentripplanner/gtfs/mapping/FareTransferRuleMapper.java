package org.opentripplanner.gtfs.mapping;

import java.time.Duration;
import java.util.Collection;
import java.util.Objects;
import org.opentripplanner.ext.fares.model.FareTransferRule;
import org.opentripplanner.graph_builder.issue.api.DataImportIssueStore;
import org.opentripplanner.transit.model.framework.FeedScopedId;

class FareTransferRuleMapper {

  public final int MISSING_VALUE = -999;

  private final IdFactory idFactory;
  private final DataImportIssueStore issueStore;
  private final FareProductMapper fareProductMapper;

  FareTransferRuleMapper(
    IdFactory idFactory,
    FareProductMapper fareProductMapper,
    DataImportIssueStore issueStore
  ) {
    this.idFactory = idFactory;
    this.fareProductMapper = fareProductMapper;
    this.issueStore = issueStore;
  }

  public Collection<FareTransferRule> map(
    Collection<org.onebusaway.gtfs.model.FareTransferRule> allRules
  ) {
    return allRules.stream().map(this::doMap).filter(Objects::nonNull).toList();
  }

  private FareTransferRule doMap(org.onebusaway.gtfs.model.FareTransferRule rhs) {
    var fareProductId = idFactory.toId(rhs.getFareProductId());
    var products = fareProductMapper.getByFareProductId(fareProductId);
    if (products.isEmpty()) {
      issueStore.add(
        "UnknownFareProductId",
        "Fare product with id %s referenced by fare transfer rule with id %s not found.".formatted(
            fareProductId,
            rhs.getId()
          )
      );
      return null;
    }

    Duration duration = null;
    if (rhs.getDurationLimit() != MISSING_VALUE) {
      duration = Duration.ofSeconds(rhs.getDurationLimit());
    }
    return new FareTransferRule(
      new FeedScopedId(fareProductId.getFeedId(), rhs.getId()),
      idFactory.toId(rhs.getFromLegGroupId()),
      idFactory.toId(rhs.getToLegGroupId()),
      rhs.getTransferCount(),
      duration,
      products
    );
  }
}
