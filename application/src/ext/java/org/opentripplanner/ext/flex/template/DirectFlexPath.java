package org.opentripplanner.ext.flex.template;

import org.opentripplanner.street.api.path.StreetPath;

/**
 * This is the result of a direct flex search. It only contains the start-time and
 * the street path. It is used by the FlexRouter to build an itinerary.
 * <p>
 * This is a simple data-transfer-object (design pattern).
 */
public record DirectFlexPath(int startTime, StreetPath path) {}
