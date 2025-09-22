package org.opentripplanner.apis.gtfs.model;

import jakarta.annotation.Nullable;
import org.opentripplanner.transit.model.timetable.EstimatedTime;

public record CallRealTime(@Nullable EstimatedTime arrival, @Nullable EstimatedTime departure) {}
