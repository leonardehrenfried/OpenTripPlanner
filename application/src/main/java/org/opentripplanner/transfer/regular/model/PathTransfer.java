package org.opentripplanner.transfer.regular.model;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Objects;
import org.locationtech.jts.geom.LineString;
import org.opentripplanner.street.api.path.StreetPath;
import org.opentripplanner.street.model.StreetMode;
import org.opentripplanner.transfer.constrained.model.ConstrainedTransfer;
import org.opentripplanner.transit.model.site.StopLocation;
import org.opentripplanner.utils.tostring.ToStringBuilder;

/**
 * Represents a transfer for a set of modes between stops with the street network path attached to
 * it.
 * <p>
 * Do not confuse this with {@link ConstrainedTransfer}.
 * <p>
 * This is the transfer of the flex transfers, which are created by the {@code
 * FlexTransferGenerator} and stored in the {@code TransferRepository}. They are used by flex
 * routing, also to and from area and group stops, and by the APIs and the debug layers. Since a
 * transfer is found for several modes, it holds its stops and the set of modes it is valid for.
 * <p>
 * The regular transfers used by Raptor are stored as {@link StreetTransferPath}s, which hold the
 * same street path. They are keyed by stop and mode in their repository, so they do not repeat the
 * from-stop and the modes.
 * <p>
 * <pre>
 * TODO: This type and {@link StreetTransferPath} will be unified. The plan is to move the flex
 *       transfers to the regular-transfer pipeline, so this type, the FlexTransferGenerator and
 *       the TransferRepository can be removed. Before that, flex needs transfers to and from area
 *       and group stops in that pipeline.
 * </pre>
 */
public class PathTransfer implements Serializable {

  public final StopLocation from;

  public final StopLocation to;

  private final double distanceMeters;

  /**
   * The street path of the transfer, or {@link StreetPath#EMPTY} if the transfer has no path, like
   * a straight-line transfer.
   */
  private final StreetPath path;

  private final EnumSet<StreetMode> modes;

  public PathTransfer(
    StopLocation from,
    StopLocation to,
    double distanceMeters,
    StreetPath path,
    EnumSet<StreetMode> modes
  ) {
    this.from = from;
    this.to = to;
    this.distanceMeters = distanceMeters;
    this.path = Objects.requireNonNull(path);
    this.modes = modes;
  }

  public double getDistanceMeters() {
    return distanceMeters;
  }

  public StreetPath getPath() {
    return path;
  }

  public EnumSet<StreetMode> getModes() {
    return EnumSet.copyOf(modes);
  }

  /** Create a new PathTransfer based on the current one with the mode added to the valid modes. */
  public PathTransfer withAddedMode(StreetMode mode) {
    EnumSet<StreetMode> newModes = EnumSet.copyOf(modes);
    newModes.add(mode);
    return new PathTransfer(from, to, distanceMeters, path, newModes);
  }

  public LineString getGeometry() {
    return path.geometry();
  }

  @Override
  public String toString() {
    return ToStringBuilder.of(getClass())
      .addObj("from", from)
      .addObj("to", to)
      .addNum("distance", distanceMeters)
      .addObj("path", path)
      .addColSize("modes", modes)
      .toString();
  }
}
