package org.opentripplanner.routing.algorithm.raptor.transit.constrainedtransfer;

import org.opentripplanner.routing.algorithm.raptor.transit.TripSchedule;
import org.opentripplanner.transit.raptor.api.transit.IntIterator;
import org.opentripplanner.transit.raptor.api.transit.RaptorTimeTable;
import org.opentripplanner.transit.raptor.api.transit.RaptorTripSchedule;


/**
 * Used to search forward and in reverse.
 */
interface ConstrainedBoardingSearchStrategy {

    /**
     * <ol>
     * <li>In a forward search return the DEPARTURE time.
     * <li>In a reverse search return the ARRIVAL time.
     * </ol>
     */
    int time(RaptorTripSchedule schedule, int stopPos);

    /**
     * <ol>
     * <li>In a forward search the time is before another time if it is in the PAST.
     * <li>In a reverse search the time is before another time if it is in the FUTURE.
     * </ol>
     */
    boolean timeIsBefore(int time0, int time1);

    /**
     * <ol>
     * <li>In a forward search iterate in departure order.
     * <li>In a reverse search iterate in reverse departure order,
     * starting with the last trip in the schedule.
     * </ol>
     */
    IntIterator scheduleIndexIterator(RaptorTimeTable<TripSchedule> timetable);
}
