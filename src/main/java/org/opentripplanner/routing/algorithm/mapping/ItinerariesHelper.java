package org.opentripplanner.routing.algorithm.mapping;

import java.util.List;
import org.opentripplanner.model.plan.Itinerary;
import org.opentripplanner.routing.api.request.RoutingRequest;

public class ItinerariesHelper {
    public static void decorateItinerariesWithRequestData(
            List<Itinerary> itineraries,
            RoutingRequest request
    ) {
        for (Itinerary it : itineraries) {
            // Communicate the fact that the only way we were able to get a response
            // was by removing a slope limit.
            it.tooSloped = request.rctx.slopeRestrictionRemoved;
        }
    }
}
