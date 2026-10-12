package org.opentripplanner.routing.via.model;

import org.opentripplanner.raptor.api.model.RaptorValueType;
import org.opentripplanner.raptor.spi.RaptorTransfer;
import org.opentripplanner.routing.cost.CostLimit;
import org.opentripplanner.street.geometry.WgsCoordinate;
import org.opentripplanner.street.model.path.StreetPath;
import org.opentripplanner.utils.time.DurationUtils;

/**
 * This class will act as a {@link RaptorTransfer} during the Raptor routing and carry enough
 * information to create an itinerary after the routing is done.

 * When routing through a coordinate, we need to generate transfers for each pair of stops
 * connected to it. In this class we keep a reference to the incoming and outgoing paths, instead
 * of connecting the two paths together into one. This approach reduces memory usage during
 * routing. Connecting paths is left to the itinerary mapping.
 */
public class ViaCoordinateTransfer implements RaptorTransfer {

  private final WgsCoordinate coordinate;
  private final int fromStopIndex;
  private final int toStopIndex;
  private final StreetPath fromPath;
  private final StreetPath toPath;
  private final int durationInSeconds;
  private final int raptorCost;

  /**
   * @param coordinate the coordinate of given via point.
   * @param fromStopIndex The Raptor stop index to use for the arriving stop.
   * @param toStopIndex The Raptor stop index to use for the boarding stop.
   * @param fromPath the street path FROM the alighting stop to the given via point.
   * @param toPath the street path TO the departing stop from the given via point.
   * @param durationInSeconds How long it takes to traverse the from and to paths, exclusive
   *                          via `minimum-wait-time`.
   * @param generalizedCostInSeconds The total cost traversing the from and to paths in the OTP
   *                                 domain generalized-cost. The unit is equivalent to seconds.
   *                                 The cost will be converted to Raptor units and cached for
   *                                 optimal performance.
   */
  public ViaCoordinateTransfer(
    WgsCoordinate coordinate,
    int fromStopIndex,
    int toStopIndex,
    StreetPath fromPath,
    StreetPath toPath,
    int durationInSeconds,
    double generalizedCostInSeconds
  ) {
    this.coordinate = coordinate;
    this.fromStopIndex = fromStopIndex;
    this.toStopIndex = toStopIndex;
    this.fromPath = fromPath;
    this.toPath = toPath;
    this.durationInSeconds = durationInSeconds;
    this.raptorCost = CostLimit.toRaptorCost(generalizedCostInSeconds);
  }

  public WgsCoordinate coordinate() {
    return coordinate;
  }

  public int fromStopIndex() {
    return fromStopIndex;
  }

  /**
   * The street path from the from-stop to the via coordinate.
   */
  public StreetPath fromPath() {
    return fromPath;
  }

  /**
   * The street path from the via coordinate to the to-stop.
   */
  public StreetPath toPath() {
    return toPath;
  }

  @Override
  public int stop() {
    return toStopIndex;
  }

  @Override
  public int c1() {
    return raptorCost;
  }

  @Override
  public int durationInSeconds() {
    return durationInSeconds;
  }

  @Override
  public String toString() {
    return (
      "{" +
      coordinate +
      " " +
      fromStopIndex +
      " ~ " +
      toStopIndex +
      " " +
      DurationUtils.durationToStr(durationInSeconds) +
      " " +
      RaptorValueType.C1.format(raptorCost) +
      "}"
    );
  }
}
