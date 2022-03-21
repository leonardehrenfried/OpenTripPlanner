package org.opentripplanner.routing.core;

import java.util.Objects;
import org.opentripplanner.routing.api.request.RoutingRequest.AccessibilityMode;
import org.opentripplanner.routing.edgetype.StreetEdge;
import org.opentripplanner.routing.api.request.RoutingRequest;

/**
 * Preferences for how to traverse the graph.
 * 
 * @author avi
 */
public class TraversalRequirements {

    /**
     * Modes allowed in graph traversal. Defaults to allowing all.
     */
    public final TraverseModeSet modes;

    /**
     * If true, trip must be wheelchair accessible.
     */
    private final AccessibilityMode accessibilityMode;

    /**
     * The maximum slope of streets for wheelchair trips.
     * 
     * ADA max wheelchair ramp slope is a good default.
     */
    private final double maxWheelchairSlope;

    /**
     * Construct from RoutingRequest.
     * 
     * @param options
     */
    public TraversalRequirements(RoutingRequest options) {
        Objects.requireNonNull(options);
        // Initialize self.
        modes = options.streetSubRequestModes.clone();
        accessibilityMode = options.accessibilityMode;
        maxWheelchairSlope = options.maxWheelchairSlope;
    }

    /** Returns true if this StreetEdge can be traversed. */
    private boolean canBeTraversedInternal(StreetEdge e) {
        if (accessibilityMode.requestsWheelchair()) {
            if (!e.isWheelchairAccessible()) {
                return false;
            }
            if (e.getMaxSlope() > maxWheelchairSlope) {
                return false;
            }
        }
        return e.canTraverse(modes);
    }

    /**
     * Returns true if this StreetEdge can be traversed.
     * Also checks if we can walk our bike on this StreetEdge.
     */
    public boolean canBeTraversed(StreetEdge e) {
        if (canBeTraversedInternal(e)) {
            return true;
        }
        return false;
    }

}
