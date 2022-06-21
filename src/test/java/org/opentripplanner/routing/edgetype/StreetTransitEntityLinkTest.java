package org.opentripplanner.routing.edgetype;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.opentripplanner.transit.model.basic.WheelchairAccessibility.NOT_POSSIBLE;
import static org.opentripplanner.transit.model.basic.WheelchairAccessibility.NO_INFORMATION;
import static org.opentripplanner.transit.model.basic.WheelchairAccessibility.POSSIBLE;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.opentripplanner.routing.api.request.RoutingRequest;
import org.opentripplanner.routing.api.request.WheelchairAccessibilityFeature;
import org.opentripplanner.routing.api.request.WheelchairAccessibilityRequest;
import org.opentripplanner.routing.core.RoutingContext;
import org.opentripplanner.routing.core.State;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.vertextype.SimpleVertex;
import org.opentripplanner.routing.vertextype.TransitStopVertex;
import org.opentripplanner.transit.model._data.TransitModelForTest;
import org.opentripplanner.transit.model.network.TransitMode;
import org.opentripplanner.transit.model.site.Stop;

class StreetTransitEntityLinkTest {

  Graph graph = new Graph();

  Stop inaccessibleStop = TransitModelForTest.stopForTest(
    "A:inaccessible",
    "wheelchair inaccessible stop",
    10.001,
    10.001,
    null,
    NOT_POSSIBLE
  );
  Stop accessibleStop = TransitModelForTest.stopForTest(
    "A:accessible",
    "wheelchair accessible stop",
    10.001,
    10.001,
    null,
    POSSIBLE
  );

  Stop unknownStop = TransitModelForTest.stopForTest(
    "A:unknown",
    "unknown",
    10.001,
    10.001,
    null,
    NO_INFORMATION
  );

  @Test
  void disallowInaccessibleStop() {
    var afterTraversal = traverse(inaccessibleStop, true);
    assertNull(afterTraversal);
  }

  @Test
  void allowAccessibleStop() {
    State afterTraversal = traverse(accessibleStop, true);

    assertNotNull(afterTraversal);
  }

  @Test
  void unknownStop() {
    var afterTraversal = traverse(unknownStop, false);
    assertNotNull(afterTraversal);

    var afterStrictTraversal = traverse(unknownStop, true);
    assertNull(afterStrictTraversal);
  }

  private State traverse(Stop stop, boolean onlyAccessible) {
    var from = new SimpleVertex(graph, "A", 10, 10);
    var to = new TransitStopVertex(graph, stop, Set.of(TransitMode.RAIL));

    var req = new RoutingRequest();
    WheelchairAccessibilityFeature feature;
    if (onlyAccessible) {
      feature = WheelchairAccessibilityFeature.ofOnlyAccessible();
    } else {
      feature = WheelchairAccessibilityFeature.ofCost(100, 100);
    }
    req.wheelchairAccessibility =
      new WheelchairAccessibilityRequest(true, feature, feature, feature, 25, 8, 10, 25);

    var ctx = new RoutingContext(req, graph, from, to);
    var state = new State(ctx);

    var edge = new StreetTransitStopLink(from, to);

    return edge.traverse(state);
  }
}
