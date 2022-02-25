package org.opentripplanner.api.model;

import java.util.List;

/**
 * Represents one instruction in walking directions. Three examples from New York City:
 * <p>
 * Turn onto Broadway from W 57th St (coming from 7th Ave): <br>
 * distance = 100 (say) <br>
 * walkDirection = RIGHT <br>
 * streetName = Broadway <br>
 * everything else null/false <br>
 * </p>
 * <p>
 * Now, turn from Broadway onto Central Park S via Columbus Circle <br>
 * distance = 200 (say) <br>
 * walkDirection = CIRCLE_COUNTERCLOCKWISE <br>
 * streetName = Central Park S <br>
 * exit = 1 (first exit) <br>
 * immediately everything else false <br>
 * </p>
 * <p>
 * Instead, go through the circle to continue on Broadway <br>
 * distance = 100 (say) <br>
 * walkDirection = CIRCLE_COUNTERCLOCKWISE <br>
 * streetName = Broadway <br>
 * exit = 3 <br>
 * stayOn = true <br>
 * everything else false <br>
 * </p>
 * */
public class ApiWalkStep {

    /**
     * The distance in meters that this step takes.
     */
    public double distance = 0;

    /**
     * The relative direction of this step.
     */
    public ApiRelativeDirection relativeDirection;

    /**
     * The name of the street.
     */
    public String streetName;

    /**
     * The absolute direction of this step.
     */
    public ApiAbsoluteDirection absoluteDirection;

    /**
     * When exiting a highway or traffic circle, the exit name/number.
     */

    public String exit;

    /**
     * Indicates whether or not a street changes direction at an intersection.
     */
    public Boolean stayOn = false;

    /**
     * This step is on an open area, such as a plaza or train platform, and thus the directions should say something like "cross"
     */
    public Boolean area = false;

    /**
     * The name of this street was generated by the system, so we should only display it once, and generally just display right/left directions
     */
    public Boolean bogusName = false;

    /**
     * The longitude of start of the step
     */
    public double lon;

    /**
     * The latitude of start of the step
     */
    public double lat;

    /**
     * The elevation profile as a comma-separated list of x,y values. x is the distance from the start of the step, y is the elevation at this
     * distance.
     */
    public String elevation;

    /**
     * Is this step walking with a bike?
     */
    public Boolean walkingBike;

    public List<ApiAlert> alerts;

    public String toString() {
        String direction = absoluteDirection.toString();
        if (relativeDirection != null) {
            direction = relativeDirection.toString();
        }
        return "WalkStep(" + direction + " on " + streetName + " for " + distance + ")";
    }

    public String streetNameNoParens() {
        int idx = streetName.indexOf('(');
        if (idx <= 0) {
            return streetName;
        }
        return streetName.substring(0, idx - 1);
    }
}
