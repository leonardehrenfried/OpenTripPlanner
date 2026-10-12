package org.opentripplanner.routing.algorithm.raptoradapter.transit;

import java.util.Objects;
import org.opentripplanner.framework.model.TimeAndCost;
import org.opentripplanner.raptor.spi.RaptorConstants;
import org.opentripplanner.routing.cost.CostLimit;
import org.opentripplanner.street.model.path.DefaultStreetPath;
import org.opentripplanner.street.model.path.StreetPath;
import org.opentripplanner.street.search.state.State;

/**
 * Default implementation of the RaptorAccessEgress interface.
 * <p>
 * Implementation note: The street path is lazy. Duration, cost and the modes used are read from
 * the final state of the street search without building the path. For egress searches
 * ({@code request.arriveBy() == true}) building the path means reversing the whole state chain,
 * which is deferred until the path is used, like when mapping a winning itinerary.
 */
public class DefaultAccessEgress implements RoutingAccessEgress {

  private final int stop;
  private final int durationInSeconds;
  private final int generalizedCost;
  private final int timePenalty;

  /** Keep this to be able to map back to itinerary */
  private final TimeAndCost penalty;

  private final StreetPath path;

  /**
   * This is public to allow unit-tests full control over the field values.
   */
  public DefaultAccessEgress(
    int stop,
    int durationInSeconds,
    int generalizedCost,
    TimeAndCost penalty,
    StreetPath path
  ) {
    this.stop = stop;
    this.durationInSeconds = durationInSeconds;
    this.generalizedCost = generalizedCost;
    this.timePenalty = penalty.isZero() ? RaptorConstants.TIME_NOT_SET : penalty.timeInSeconds();
    this.penalty = penalty;
    this.path = Objects.requireNonNull(path);
  }

  public DefaultAccessEgress(int stop, StreetPath path) {
    this(
      stop,
      (int) path.duration().toSeconds(),
      CostLimit.toRaptorCost(path.generalizedCost()),
      TimeAndCost.ZERO,
      path
    );
  }

  /**
   * Create an access/egress from the final state of a street search.
   */
  public DefaultAccessEgress(int stop, State finalState) {
    this(stop, new DefaultStreetPath(finalState));
  }

  /**
   * Create a new access/egress for the same stop and street search as {@code other}. Duration and
   * cost are derived from the street search again, so any penalty of {@code other} is dropped.
   */
  protected DefaultAccessEgress(DefaultAccessEgress other) {
    this(other.stop, other.path);
  }

  protected DefaultAccessEgress(DefaultAccessEgress other, TimeAndCost penalty) {
    // In the API we have a cost associated with the time-penalty. In Raptor, there is no
    // association between the time-penalty and the cost. So, we add the time-penalty cost to
    // the generalized cost here. In logic later on, we will remove it.
    this(
      other.stop(),
      other.durationInSeconds(),
      other.c1() + penalty.cost().toCentiSeconds(),
      penalty,
      other.path
    );
    if (other.penalty() != TimeAndCost.ZERO) {
      throw new IllegalStateException("Can not add penalty twice...");
    }
  }

  @Override
  public int durationInSeconds() {
    return durationInSeconds;
  }

  @Override
  public int timePenalty() {
    return timePenalty;
  }

  @Override
  public int stop() {
    return stop;
  }

  @Override
  public int c1() {
    return generalizedCost;
  }

  @Override
  public boolean hasOpeningHours() {
    return false;
  }

  /**
   * The street path of this access/egress in chronological order.
   */
  public StreetPath streetPath() {
    return path;
  }

  @Override
  public boolean isRentingVehicleFromStation() {
    return path.arrivesWithRentedVehicleFromStation();
  }

  @Override
  public boolean containsModeCar() {
    return path.containsModeCar();
  }

  @Override
  public boolean isWalkOnly() {
    return path.containsOnlyWalkMode();
  }

  @Override
  public TimeAndCost penalty() {
    return penalty;
  }

  /**
   * Return a new copy of this with the requested penalty.
   * <p>
   * OVERRIDE THIS IF KEEPING THE TYPE IS IMPORTANT!
   */
  @Override
  public RoutingAccessEgress withPenalty(TimeAndCost penalty) {
    return new DefaultAccessEgress(this, penalty);
  }

  @Override
  public int earliestDepartureTime(int requestedDepartureTime) {
    return requestedDepartureTime;
  }

  @Override
  public int latestArrivalTime(int requestedArrivalTime) {
    return requestedArrivalTime;
  }

  @Override
  public String toString() {
    return asString(true, true, summary());
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    // We check the contract of DefaultAccessEgress used for routing for equality, we do not care
    // if the entries are different implementation or have different AStar paths(lastState).
    if (!(o instanceof RoutingAccessEgress that)) {
      return false;
    }
    return (
      stop() == that.stop() &&
      durationInSeconds() == that.durationInSeconds() &&
      c1() == that.c1() &&
      penalty().equals(that.penalty())
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(stop, durationInSeconds, generalizedCost, penalty);
  }

  /**
   * Include summary information in toString. We only include information relevant for using this
   * in routing (not latestState).
   */
  private String summary() {
    return penalty.isZero() ? null : "w/penalty" + penalty;
  }
}
