package org.opentripplanner.graph_builder.issues;

import org.locationtech.jts.geom.Geometry;
import org.opentripplanner.framework.geometry.GeometryUtils;
import org.opentripplanner.graph_builder.issue.api.DataImportIssue;
import org.opentripplanner.osm.model.OsmEntity;
import org.opentripplanner.osm.model.OsmNode;

public record DisconnectedOsmNode(OsmNode node, OsmEntity way, OsmEntity area)
  implements DataImportIssue {
  private static final String FMT = "Node %s in way %s is coincident but disconnected with area %s";
  private static final String HTMLFMT =
    "Node<a href='%s'>'%s'</a> in way <a href='%s'>'%s'</a> is coincident but disconnected with area <a href='%s'>'%s'</a>";

  @Override
  public String getMessage() {
    return FMT.formatted(node.getId(), way.getId(), area.getId());
  }

  @Override
  public String getHTMLMessage() {
    return HTMLFMT.formatted(
      node.url(),
      node.getId(),
      way.url(),
      way.getId(),
      area.url(),
      area.getId()
    );
  }

  @Override
  public Geometry getGeometry() {
    return GeometryUtils.getGeometryFactory().createPoint(node.getCoordinate());
  }
}
