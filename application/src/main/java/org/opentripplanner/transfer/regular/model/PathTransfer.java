package org.opentripplanner.transfer.regular.model;

import java.io.Serializable;
import java.util.EnumSet;
import org.jspecify.annotations.Nullable;
import org.locationtech.jts.geom.LineString;
import org.opentripplanner.street.api.path.StreetPath;
import org.opentripplanner.street.geometry.GeometryUtils;
import org.opentripplanner.street.model.StreetMode;
import org.opentripplanner.transfer.constrained.model.ConstrainedTransfer;
import org.opentripplanner.transit.model.site.StopLocation;
import org.opentripplanner.utils.tostring.ToStringBuilder;

/**
 * Represents a transfer for a set of modes between stops with the street network path attached to
 * it.
 * <p>
 * Do not confuse this with {@link ConstrainedTransfer}.
 *
 * <p>
 * <pre>
 * TODO Should we just store the NearbyStop as a field here, or even switch to using it instead
 *      where this class is used
 * </pre>
 */
public class PathTransfer implements Serializable {

  public final StopLocation from;

  public final StopLocation to;

  private final double distanceMeters;

  /**
   * The street path of the transfer, or null if the transfer has no path, like a straight-line
   * transfer.
   */
  @Nullable
  private final StreetPath path;

  private final EnumSet<StreetMode> modes;

  public PathTransfer(
    StopLocation from,
    StopLocation to,
    double distanceMeters,
    @Nullable StreetPath path,
    EnumSet<StreetMode> modes
  ) {
    this.from = from;
    this.to = to;
    this.distanceMeters = distanceMeters;
    this.path = path;
    this.modes = modes;
  }

  public double getDistanceMeters() {
    return distanceMeters;
  }

  @Nullable
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
    if (path == null) {
      return GeometryUtils.getGeometryFactory().createLineString();
    } else {
      return path.geometry();
    }
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
