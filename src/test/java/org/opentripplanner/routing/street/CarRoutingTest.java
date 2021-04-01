package org.opentripplanner.routing.street;

import org.junit.Assert;
import org.junit.Test;
import org.opentripplanner.ConstantsForTests;
import org.opentripplanner.model.GenericLocation;
import org.opentripplanner.routing.algorithm.mapping.GraphPathToItineraryMapper;
import org.opentripplanner.routing.api.request.RoutingRequest;
import org.opentripplanner.routing.core.TraverseMode;
import org.opentripplanner.routing.core.TraverseModeSet;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.impl.GraphPathFinder;
import org.opentripplanner.standalone.config.RouterConfig;
import org.opentripplanner.standalone.server.Router;

import java.time.Instant;

import static org.opentripplanner.PolylineAssert.assertThatPolylinesAreEqual;


public class CarRoutingTest {
    static long dateTime = Instant.now().toEpochMilli();

    private static String computePolyline(Graph graph, GenericLocation from, GenericLocation to) {
        RoutingRequest request = new RoutingRequest();
        request.dateTime = dateTime;
        request.from = from;
        request.to = to;

        request.streetSubRequestModes = new TraverseModeSet(TraverseMode.CAR);

        request.setRoutingContext(graph);

        var gpf = new GraphPathFinder(new Router(graph, RouterConfig.DEFAULT));
        var paths = gpf.graphPathFinderEntryPoint(request);

        var itineraries = GraphPathToItineraryMapper.mapItineraries(paths, request);
        // make sure that we only get CAR legs
        itineraries.forEach(i -> i.legs.forEach(l -> Assert.assertEquals(l.mode, TraverseMode.CAR)));
        return itineraries.get(0).legs.get(0).legGeometry.getPoints();
    }

    @Test
    public void shouldBeAbleToTurnIntoAufDemGraben() {
        var hindenburgStrUnderConstruction =  TestGraphBuilder.buildOsmGraph(ConstantsForTests.HERRENBERG_HINDENBURG_STR_UNDER_CONSTRUCTION_OSM);

        var gueltsteinerStr = new GenericLocation(48.59240, 8.87024);
        var aufDemGraben = new GenericLocation(48.59487, 8.87133);

        var polyline = computePolyline(hindenburgStrUnderConstruction, gueltsteinerStr, aufDemGraben);

        assertThatPolylinesAreEqual(polyline, "ouqgH}mcu@gAE]U}BaA]Q}@]uAs@[SAm@Ee@AUEi@XEQkBQ?Bz@Dt@Dh@@TGBC@KBSHGx@");
    }

    @Test
    public void shouldRespectNoThroughTraffic() {
        var herrenbergGraph = TestGraphBuilder.buildOsmGraph(ConstantsForTests.HERRENBERG_OSM);
        var mozartStr = new GenericLocation(48.59521, 8.88391);
        var fritzLeharStr = new GenericLocation(48.59460, 8.88291);

        var polyline1 = computePolyline(herrenbergGraph, mozartStr, fritzLeharStr);
        assertThatPolylinesAreEqual(polyline1, "_grgHkcfu@OjBC\\ARGjAKzAfBz@j@n@Rk@E}D");

        var polyline2 = computePolyline(herrenbergGraph, fritzLeharStr, mozartStr);
        assertThatPolylinesAreEqual(polyline2, "gcrgHc}eu@D|DSj@k@o@gB{@J{AFkA@SB]NkB");
    }
}
