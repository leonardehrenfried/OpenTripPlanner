package org.opentripplanner.routing.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.opentripplanner.routing.core.TraverseMode.CAR;
import static org.opentripplanner.transit.model._data.TransitModelForTest.agency;
import static org.opentripplanner.transit.model._data.TransitModelForTest.route;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.opentripplanner.model.GenericLocation;
import org.opentripplanner.routing.api.request.RoutingRequest;
import org.opentripplanner.transit.model._data.TransitModelForTest;
import org.opentripplanner.transit.model.framework.FeedScopedId;
import org.opentripplanner.transit.model.network.Route;
import org.opentripplanner.transit.model.organization.Agency;

public class RoutingRequestTest {

  private static final FeedScopedId AGENCY_ID = TransitModelForTest.id("A1");
  private static final FeedScopedId ROUTE_ID = TransitModelForTest.id("R1");
  private static final FeedScopedId OTHER_ID = TransitModelForTest.id("X");
  public static final String TIMEZONE = "Europe/Paris";

  @Test
  public void testRequest() {
    RoutingRequest request = new RoutingRequest();

    request.addMode(CAR);
    assertTrue(request.streetSubRequestModes.getCar());
    request.removeMode(CAR);
    assertFalse(request.streetSubRequestModes.getCar());

    request.setStreetSubRequestModes(new TraverseModeSet(TraverseMode.BICYCLE, TraverseMode.WALK));
    assertFalse(request.streetSubRequestModes.getCar());
    assertTrue(request.streetSubRequestModes.getBicycle());
    assertTrue(request.streetSubRequestModes.getWalk());
  }

  @Test
  public void testIntermediatePlaces() {
    RoutingRequest req = new RoutingRequest();
    assertFalse(req.hasIntermediatePlaces());

    req.clearIntermediatePlaces();
    assertFalse(req.hasIntermediatePlaces());

    req.addIntermediatePlace(randomLocation());
    assertTrue(req.hasIntermediatePlaces());

    req.clearIntermediatePlaces();
    assertFalse(req.hasIntermediatePlaces());

    req.addIntermediatePlace(randomLocation());
    req.addIntermediatePlace(randomLocation());
    assertTrue(req.hasIntermediatePlaces());
  }

  @Test
  public void shouldCloneObjectFields() {
    var req = new RoutingRequest();

    var clone = req.clone();

    assertNotSame(clone, req);
    assertNotSame(clone.itineraryFilters, req.itineraryFilters);
    assertNotSame(clone.raptorDebugging, req.raptorDebugging);
    assertNotSame(clone.raptorOptions, req.raptorOptions);

    assertEquals(50, req.numItineraries);
    assertEquals(50, clone.numItineraries);
  }

  @Test
  public void testPreferencesPenaltyForRoute() {
    Agency agency = agency("A").copy().withId(AGENCY_ID).withTimezone(TIMEZONE).build();
    Route route = route(ROUTE_ID.getId()).withShortName("R").withAgency(agency).build();

    Route otherRoute = route
      .copy()
      .withId(OTHER_ID)
      .withShortName("OtherR")
      .withAgency(agency.copy().withId(OTHER_ID).withName("OtherA").build())
      .build();

    List<String> testCases = List.of(
      // !prefAgency | !prefRoute | unPrefA | unPrefR | expected cost
      "       -      |      -     |    -    |    -    |     0",
      "       -      |      -     |    -    |    x    |   300",
      "       -      |      -     |    x    |    -    |   300",
      "       -      |      x     |    -    |    -    |   300",
      "       x      |      -     |    -    |    -    |   300",
      "       -      |      -     |    x    |    x    |   300",
      "       x      |      x     |    -    |    -    |   300",
      "       x      |      -     |    -    |    x    |   600",
      "       -      |      x     |    x    |    -    |   600",
      "       x      |      x     |    x    |    x    |   600"
    );

    for (String it : testCases) {
      RoutePenaltyTC tc = new RoutePenaltyTC(it);
      RoutingRequest routingRequest = tc.createRoutingRequest();

      assertEquals(
        tc.expectedCost,
        routingRequest.preferencesPenaltyForRoute(route),
        tc.toString()
      );

      if (tc.prefAgency || tc.prefRoute) {
        assertEquals(0, routingRequest.preferencesPenaltyForRoute(otherRoute), tc.toString());
      }
    }
  }

  private GenericLocation randomLocation() {
    return new GenericLocation(Math.random(), Math.random());
  }

  private static class RoutePenaltyTC {

    final boolean prefAgency;
    final boolean prefRoute;
    final boolean unPrefAgency;
    final boolean unPrefRoute;
    public final int expectedCost;

    RoutePenaltyTC(String input) {
      String[] cells = input.replace(" ", "").split("\\|");
      this.prefAgency = "x".equalsIgnoreCase(cells[0]);
      this.prefRoute = "x".equalsIgnoreCase(cells[1]);
      this.unPrefAgency = "x".equalsIgnoreCase(cells[2]);
      this.unPrefRoute = "x".equalsIgnoreCase(cells[3]);
      this.expectedCost = Integer.parseInt(cells[4]);
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder();
      if (prefAgency) {
        sb.append(", prefAgency=X");
      }
      if (prefRoute) {
        sb.append(", prefRoute=X");
      }
      if (unPrefAgency) {
        sb.append(", unPrefAgency=X");
      }
      if (unPrefRoute) {
        sb.append(", unPrefRoute=X");
      }

      return "RoutePenaltyTC {" + sb.substring(sb.length() == 0 ? 0 : 2) + "}";
    }

    RoutingRequest createRoutingRequest() {
      RoutingRequest request = new RoutingRequest();
      if (prefAgency) {
        request.setPreferredAgencies(List.of(OTHER_ID));
      }
      if (prefRoute) {
        request.setPreferredRoutes(List.of(OTHER_ID));
      }
      if (unPrefAgency) {
        request.setUnpreferredAgencies(List.of(AGENCY_ID));
      }
      if (unPrefRoute) {
        request.setUnpreferredRoutes(List.of(ROUTE_ID));
      }
      return request;
    }
  }
}
