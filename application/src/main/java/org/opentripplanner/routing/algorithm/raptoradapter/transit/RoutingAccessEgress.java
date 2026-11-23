package org.opentripplanner.routing.algorithm.raptoradapter.transit;

import org.opentripplanner.framework.model.TimeAndCost;
import org.opentripplanner.raptor.api.model.RaptorAccessEgress;

/**
 * Encapsulate information about an access or egress path. This interface extends
 * {@link RaptorAccessEgress} with methods relevant only to street routing and access/egress
 * filtering.
 */
public interface RoutingAccessEgress extends RaptorAccessEgress {
  /**
   * Return a new copy of this with the requested penalty.
   * <p>
   * OVERRIDE THIS IF KEEPING THE TYPE IS IMPORTANT!
   */
  RoutingAccessEgress withPenalty(TimeAndCost penalty);

  /**
   * Return true if all edges are traversed on foot.
   */
  boolean isWalkOnly();

  TimeAndCost penalty();
}
