package org.opentripplanner.graph_builder.module.islandpruning.moduletests;

import static com.google.common.truth.Truth.assertWithMessage;
import static org.opentripplanner.street.model.StreetModelForTest.bidirectional;
import static org.opentripplanner.street.model.StreetModelForTest.intersectionVertex;
import static org.opentripplanner.street.model.StreetModelForTest.streetEdgeBuilder;
import static org.opentripplanner.street.model.StreetTraversalPermission.ALL;

import org.junit.jupiter.api.Test;
import org.opentripplanner.graph_builder.module.islandpruning.IslandPruningEnvironment;
import org.opentripplanner.graph_builder.module.islandpruning.IslandPruningParameters;

/**
 * Pruning is done independently per traverse mode. A dead-end reachable from the main street
 * network only via a connector that is "no thru traffic" for cars (e.g. a bollard), but fully
 * open to walking and cycling, must not be pruned or converted to no-thru for any mode: for CAR
 * it is still reachable as a destination (so it becomes no-thru rather than being removed), while
 * for WALK and BICYCLE the dead-end is just a normal, fully connected part of the network and is
 * left untouched.
 */
class CarOnlyDeadEndBecomesNoThruTest {

  @Test
  void carOnlyConnectorProducesCarOnlyNoThru() {
    // Main street network: a small square of four intersections, large enough to never be
    // considered for pruning.
    var a = intersectionVertex(0, 0);
    var b = intersectionVertex(0, 1);
    var c = intersectionVertex(1, 1);
    var d = intersectionVertex(1, 0);

    // Dead-end tail, reachable from the main square by car only via a destination-only
    // ("no thru traffic") connector, but freely walkable and bikeable.
    var e = intersectionVertex(2, 0);
    var f = intersectionVertex(3, 0);

    bidirectional(a, b, ALL);
    bidirectional(b, c, ALL);
    bidirectional(c, d, ALL);
    bidirectional(d, a, ALL);
    bidirectional(e, f, ALL);

    // Connector: already destination-only for cars, e.g. tagged `motor_vehicle=destination` in
    // OSM, but otherwise open to all modes.
    streetEdgeBuilder(d, e, 1, ALL).withMotorVehicleNoThruTraffic(true).buildAndConnect();
    streetEdgeBuilder(e, d, 1, ALL).withMotorVehicleNoThruTraffic(true).buildAndConnect();

    // Dead end has 2 street vertices (e, f), which is below the threshold of 3.
    var summarizer = IslandPruningEnvironment.of(a, b, c, d, e, f).prune(
      IslandPruningParameters.DEFAULTS
    );

    assertWithMessage("Unexpected edges. Check graph at %s", summarizer.geoJsonUrl())
      .that(summarizer.summarizeEdges())
      .containsExactly(
        // main square: untouched
        "(0,0) → (0,1) ALL ♿✅",
        "(0,1) → (0,0) ALL ♿✅",
        "(0,1) → (1,1) ALL ♿✅",
        "(1,1) → (0,1) ALL ♿✅",
        "(1,1) → (1,0) ALL ♿✅",
        "(1,0) → (1,1) ALL ♿✅",
        "(1,0) → (0,0) ALL ♿✅",
        "(0,0) → (1,0) ALL ♿✅",
        // connector: already destination-only for cars
        "(1,0) → (2,0) ALL ♿✅ noThru=CAR",
        "(2,0) → (1,0) ALL ♿✅ noThru=CAR",
        // dead-end edge: converted to no-thru-traffic for CAR only, still fully walkable/bikeable
        "(2,0) → (3,0) ALL ♿✅ noThru=CAR",
        "(3,0) → (2,0) ALL ♿✅ noThru=CAR"
      );
  }
}
