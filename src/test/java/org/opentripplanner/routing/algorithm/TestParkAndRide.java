package org.opentripplanner.routing.algorithm;

import junit.framework.TestCase;
import org.opentripplanner.common.geometry.GeometryUtils;
import org.opentripplanner.model.FeedScopedId;
import org.opentripplanner.routing.algorithm.astar.AStar;
import org.opentripplanner.routing.algorithm.astar.strategies.EuclideanRemainingWeightHeuristic;
import org.opentripplanner.routing.api.request.RoutingRequest;
import org.opentripplanner.routing.core.State;
import org.opentripplanner.routing.core.TraverseMode;
import org.opentripplanner.routing.core.TraverseModeSet;
import org.opentripplanner.routing.edgetype.VehicleParkingEdge;
import org.opentripplanner.routing.edgetype.ParkAndRideEdge;
import org.opentripplanner.routing.edgetype.ParkAndRideLinkEdge;
import org.opentripplanner.routing.edgetype.StreetVehicleParkingLink;
import org.opentripplanner.routing.edgetype.StreetEdge;
import org.opentripplanner.routing.edgetype.StreetTraversalPermission;
import org.opentripplanner.routing.graph.Edge;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.spt.GraphPath;
import org.opentripplanner.routing.spt.ShortestPathTree;
import org.opentripplanner.routing.vehicle_parking.VehicleParking;
import org.opentripplanner.routing.vertextype.VehicleParkingVertex;
import org.opentripplanner.routing.vertextype.IntersectionVertex;
import org.opentripplanner.routing.vertextype.ParkAndRideVertex;
import org.opentripplanner.routing.vertextype.StreetVertex;
import org.opentripplanner.util.NonLocalizedString;

/**
 * Test P+R (both car P+R and bike P+R).
 * 
 * @author laurent
 */
public class TestParkAndRide extends TestCase {

    private static final String TEST_FEED_ID = "testFeed";

    private Graph graph;
    private StreetVertex A,B,C,D;

    @Override
    protected void setUp() throws Exception {
        graph = new Graph();

        // Generate a very simple graph
        A = new IntersectionVertex(graph, "A", 0.000, 45, "A");
        B = new IntersectionVertex(graph, "B", 0.001, 45, "B");
        C = new IntersectionVertex(graph, "C", 0.002, 45, "C");
        D = new IntersectionVertex(graph, "D", 0.003, 45, "D");

        @SuppressWarnings("unused")
        Edge driveOnly = new StreetEdge(A, B, GeometryUtils.makeLineString(0.000, 45, 0.001, 45),
                "AB street", 87, StreetTraversalPermission.CAR, false);

        @SuppressWarnings("unused")
        Edge walkAndBike = new StreetEdge(B, C, GeometryUtils.makeLineString(0.001, 45, 0.002,
                45), "BC street", 87, StreetTraversalPermission.PEDESTRIAN_AND_BICYCLE, false);

        @SuppressWarnings("unused")
        Edge walkOnly = new StreetEdge(C, D, GeometryUtils.makeLineString(0.002, 45, 0.003,
                45), "CD street", 87, StreetTraversalPermission.PEDESTRIAN, false);
    }
    
    public void testCar() {

        AStar aStar = new AStar();

        // It is impossible to get from A to C in WALK mode,
        RoutingRequest options = new RoutingRequest(new TraverseModeSet(TraverseMode.WALK));
        options.setRoutingContext(graph, A, C);
        ShortestPathTree tree = aStar.getShortestPathTree(options);
        GraphPath path = tree.getPath(C, false);
        assertNull(path);

        // or CAR+WALK (no P+R).
        options = new RoutingRequest(new TraverseModeSet(TraverseMode.WALK,TraverseMode.CAR));
        options.setRoutingContext(graph, A, C);
        tree = aStar.getShortestPathTree(options);
        path = tree.getPath(C, false);
        assertNull(path);

        // So we Add a P+R at B.
        ParkAndRideVertex PRB = new ParkAndRideVertex(graph, "P+R", "P+R.B", 0.001, 45.00001,
                new NonLocalizedString("P+R B"));
        new ParkAndRideEdge(PRB);
        new ParkAndRideLinkEdge(PRB, B);
        new ParkAndRideLinkEdge(B, PRB);

        // But it is still impossible to get from A to C by WALK only
        // (AB is CAR only).
        options = new RoutingRequest(new TraverseModeSet(TraverseMode.WALK));
        options.setRoutingContext(graph, A, C);
        tree = aStar.getShortestPathTree(options);
        path = tree.getPath(C, false);
        assertNull(path);
        
        // Or CAR only (BC is WALK only).
        options = new RoutingRequest(new TraverseModeSet(TraverseMode.CAR));
        options.setRoutingContext(graph, A, C);
        tree = aStar.getShortestPathTree(options);
        path = tree.getPath(C, false);
        assertNull(path);

        // But we can go from A to C with CAR+WALK mode using P+R. arriveBy false
        options = new RoutingRequest(new TraverseModeSet(TraverseMode.WALK,TraverseMode.CAR,TraverseMode.TRANSIT));
        options.parkAndRide = true;
        //options.arriveBy
        options.setRoutingContext(graph, A, C);
        tree = aStar.getShortestPathTree(options);
        path = tree.getPath(C, false);
        assertNotNull(path);

        // But we can go from A to C with CAR+WALK mode using P+R. arriveBy true
        options = new RoutingRequest(new TraverseModeSet(TraverseMode.WALK,TraverseMode.CAR,TraverseMode.TRANSIT));
        options.parkAndRide = true;
        options.setArriveBy(true);
        options.setRoutingContext(graph, A, C);
        tree = aStar.getShortestPathTree(options);
        path = tree.getPath(A, false);
        assertNotNull(path);


        // But we can go from A to C with CAR+WALK mode using P+R. arriveBy true interleavedBidiHeuristic
        options = new RoutingRequest(new TraverseModeSet(TraverseMode.WALK,TraverseMode.CAR,TraverseMode.TRANSIT));
        options.parkAndRide = true;
        options.setArriveBy(true);
        options.setRoutingContext(graph, A, C);
        options.rctx.remainingWeightHeuristic = new EuclideanRemainingWeightHeuristic();
        tree = aStar.getShortestPathTree(options);
        path = tree.getPath(A, false);
        assertNotNull(path);

        // But we can go from A to C with CAR+WALK mode using P+R. arriveBy false interleavedBidiHeuristic
        options = new RoutingRequest(new TraverseModeSet(TraverseMode.WALK,TraverseMode.CAR,TraverseMode.TRANSIT));
        options.parkAndRide = true;
        //options.arriveBy
        options.setRoutingContext(graph, A, C);
        options.rctx.remainingWeightHeuristic = new EuclideanRemainingWeightHeuristic();
        tree = aStar.getShortestPathTree(options);
        path = tree.getPath(C, false);
        assertNotNull(path);
    }

    public void testBike() {

        AStar aStar = new AStar();

        // Impossible to get from B to D in BIKE+WALK (no bike P+R).
        RoutingRequest options = new RoutingRequest(new TraverseModeSet(TraverseMode.BICYCLE,TraverseMode.TRANSIT));
        options.bikeParkAndRide = true;
        options.setRoutingContext(graph, B, D);
        ShortestPathTree tree = aStar.getShortestPathTree(options);
        GraphPath path = tree.getPath(D, false);
        assertNull(path);

        // So we add a bike P+R at C.
        var vehicleParkingName = new NonLocalizedString("Bike Park C");
        VehicleParking bpc = VehicleParking.builder()
            .id(new FeedScopedId(TEST_FEED_ID, "bpc"))
            .name(vehicleParkingName)
            .x(0.002)
            .y(45.00001)
            .bicyclePlaces(true)
            .build();
        VehicleParkingVertex BPRC = new VehicleParkingVertex(graph, bpc);
        new VehicleParkingEdge(BPRC);
        new StreetVehicleParkingLink(BPRC, C);
        new StreetVehicleParkingLink(C, BPRC);

        // Still impossible from B to D by bike only (CD is WALK only).
        options = new RoutingRequest(new TraverseModeSet(TraverseMode.BICYCLE));
        options.setRoutingContext(graph, B, D);
        tree = aStar.getShortestPathTree(options);
        path = tree.getPath(D, false);
        assertNotNull(path);
        State s = tree.getState(D);
        assertFalse(s.isBikeParked());
        // TODO backWalkingBike flag is broken
        // assertTrue(s.isBackWalkingBike());
        assertSame(s.getBackMode(), TraverseMode.WALK);

        // But we can go from B to D using bike P+R.
        options = new RoutingRequest(new TraverseModeSet(TraverseMode.BICYCLE,TraverseMode.WALK,TraverseMode.TRANSIT));
        options.bikeParkAndRide = true;
        options.setRoutingContext(graph, B, D);
        tree = aStar.getShortestPathTree(options);
        path = tree.getPath(D, false);
        assertNotNull(path);
        s = tree.getState(D);
        assertTrue(s.isBikeParked());
        assertFalse(s.isBackWalkingBike());
    }
}
