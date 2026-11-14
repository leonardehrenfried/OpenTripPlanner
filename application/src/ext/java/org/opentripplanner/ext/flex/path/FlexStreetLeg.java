package org.opentripplanner.ext.flex.path;

import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.locationtech.jts.geom.LineString;
import org.opentripplanner.street.model.elevation.ElevationProfile;
import org.opentripplanner.street.model.path.StreetLeg;
import org.opentripplanner.street.model.path.StreetLegPlace;
import org.opentripplanner.street.model.path.step.StreetStep;
import org.opentripplanner.street.search.TraverseMode;
import org.opentripplanner.utils.lang.IntUtils;

/**
 * The single leg of a {@link FlexStreetPath}: a ride on a flexible trip. It has no elevation data
 * and no turn-by-turn directions.
 */
public final class FlexStreetLeg implements StreetLeg {

  private final FlexStreetPath path;

  FlexStreetLeg(FlexStreetPath path) {
    this.path = path;
  }

  /**
   * The flex ride this leg is part of.
   */
  public FlexStreetPath path() {
    return path;
  }

  @Override
  public Instant startTime() {
    return path.legStartTime();
  }

  @Override
  public Instant endTime() {
    return startTime().plus(path.duration());
  }

  @Override
  public TraverseMode computeMode() {
    return TraverseMode.FLEX;
  }

  @Override
  public StreetLegPlace from() {
    var vertex = path.fromVertex();
    return new StreetLegPlace(vertex, vertex.getName(), false);
  }

  @Override
  public StreetLegPlace to() {
    var vertex = path.toVertex();
    return new StreetLegPlace(vertex, vertex.getName(), false);
  }

  @Override
  public double distanceMeters() {
    return path.distanceMeters();
  }

  @Override
  public int generalizedCost() {
    return IntUtils.round(path.generalizedCost());
  }

  @Override
  public LineString geometry() {
    return path.geometry();
  }

  @Override
  @Nullable
  public ElevationProfile elevationProfile() {
    return null;
  }

  @Override
  public List<StreetStep> computeSteps() {
    return List.of();
  }

  @Override
  public boolean rentedVehicle() {
    return false;
  }

  @Override
  @Nullable
  public String vehicleRentalNetwork() {
    return null;
  }
}
