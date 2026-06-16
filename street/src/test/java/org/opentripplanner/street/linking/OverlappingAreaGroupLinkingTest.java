package org.opentripplanner.street.linking;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.opentripplanner.street.model.StreetModelFactory.createAreaEdge;
import static org.opentripplanner.street.model.StreetModelFactory.intersectionVertex;
import static org.opentripplanner.street.model.StreetModelFactory.transitStopVertex;
import static org.opentripplanner.street.model.StreetTraversalPermission.PEDESTRIAN;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.opentripplanner.core.model.i18n.LocalizedString;
import org.opentripplanner.street.geometry.GeometryUtils;
import org.opentripplanner.street.model.StreetTraversalPermission;
import org.opentripplanner.street.model.edge.Area;
import org.opentripplanner.street.model.edge.AreaGroup;
import org.opentripplanner.street.model.vertex.IntersectionVertex;

/**
 * Tests that a visibility edge crossing two sub-areas inherits the worst-case properties
 * (permission intersection, wheelchair AND) from both areas.
 */
class OverlappingAreaGroupLinkingTest {

  private static final GeometryFactory GEOMETRY_FACTORY = GeometryUtils.getGeometryFactory();

  /**
   * Two horizontal strips tile the combined rectangle:
   *
   * A stop below the bottom edge is linked to the ring. The split point on the
   * bottom edge at roughly x=10.006 gets a visibility edge to v0. That edge
   * travels bottom-to-top, crossing Area A (bottom strip) then Area B (top strip).
   * Merged result: PEDESTRIAN permission, wheelchair=false.
   */
  @Test
  void visibilityEdgeCrossingTwoAreasInheritsWorstCaseProperties() {
    // corners of the combined rectangle
    Coordinate tl = new Coordinate(10.0, 60.002);
    Coordinate tr = new Coordinate(10.012, 60.002);
    Coordinate br = new Coordinate(10.012, 60.0);
    Coordinate bl = new Coordinate(10.0, 60.0);
    // midline separating the two strips
    Coordinate ml = new Coordinate(10.0, 60.001);
    Coordinate mr = new Coordinate(10.012, 60.001);

    // label → intersectionVertex(label, lat, lon)
    var v0 = intersectionVertex(tl);
    var v1 = intersectionVertex(tr);
    var v2 = intersectionVertex(br);
    var v3 = intersectionVertex(bl);

    Polygon combined = GEOMETRY_FACTORY.createPolygon(new Coordinate[] { tl, bl, br, tr, tl });
    AreaGroup areaGroup = new AreaGroup(combined);
    // v0 is the only visibility vertex — so linking creates exactly one visibility edge per
    // split point, going from the bottom edge to v0 and crossing both strips.
    areaGroup.addVisibilityVertices(Set.of(v0));

    // Area A: bottom strip (y: 60.0–60.001) — wheelchair accessible
    var polygonA = GEOMETRY_FACTORY.createPolygon(new Coordinate[] { ml, bl, br, mr, ml });
    Area areaA = new Area();
    areaA.setName(new LocalizedString("area-a"));
    areaA.setPermission(StreetTraversalPermission.PEDESTRIAN_AND_BICYCLE);
    areaA.setWalkSafety(1.0f);
    areaA.setBicycleSafety(1.0f);
    areaA.setGeometry(polygonA);
    areaA.setWheelchairAccessible(true);
    areaGroup.addArea(areaA);

    // Area B: top strip (y: 60.001–60.002) — NOT wheelchair accessible
    Polygon polygonB = GEOMETRY_FACTORY.createPolygon(new Coordinate[] { tl, ml, mr, tr, tl });
    Area areaB = new Area();
    areaB.setName(new LocalizedString("area-b"));
    areaB.setPermission(PEDESTRIAN);
    areaB.setWalkSafety(4.0f);
    areaB.setBicycleSafety(4.0f);
    areaB.setGeometry(polygonB);
    areaB.setWheelchairAccessible(false);
    areaGroup.addArea(areaB);

    // ring edges must be built before LinkingEnvironment so they are indexed with the vertices
    IntersectionVertex[] ring = { v0, v1, v2, v3 };
    for (int i = 0; i < ring.length; i++) {
      var from = ring[i];
      var to = ring[(i + 1) % ring.length];
      createAreaEdge(from, to, areaGroup).buildAndConnect();
      createAreaEdge(to, from, areaGroup).buildAndConnect();
    }

    var stopVertex = transitStopVertex(0, new Coordinate(10.006, 60.0005));

    var env = new LinkingEnvironment(v0, v1, v2, v3, stopVertex);

    assertThat(env.graph().summarizeEdges()).containsExactly(
      "(60,10.012) → (60.002,10.012) PEDESTRIAN_AND_BICYCLE ♿✅",
      "(60,10.012) → (60,10) PEDESTRIAN_AND_BICYCLE ♿✅",
      "(60.002,10.012) → (60,10.012) PEDESTRIAN_AND_BICYCLE ♿✅",
      "(60,10) → (60,10.012) PEDESTRIAN_AND_BICYCLE ♿✅",
      "(60.002,10) → (60.002,10.012) PEDESTRIAN_AND_BICYCLE ♿✅",
      "(60.002,10) → (60,10) PEDESTRIAN_AND_BICYCLE ♿✅",
      "(60.002,10.012) → (60.002,10) PEDESTRIAN_AND_BICYCLE ♿✅",
      "(60,10) → (60.002,10) PEDESTRIAN_AND_BICYCLE ♿✅"
    );

    env.linkVertexPermanently(stopVertex);

    assertWithMessage("Unexpected edges. Check %s", env.graph().geoJsonUrl())
      .that(env.graph().summarizeEdges())
      .containsExactly(
        // the edges that cross the top polygon are not wheelchair-accessible and pedestrian only
        "(60.0005,10.006) → (60.002,10) PEDESTRIAN ♿❌",
        "(60.002,10) → (60.0005,10.006) PEDESTRIAN ♿❌",
        // links
        "(60.0005,10.006)[street:0] linked to (60.0005,10.006)",
        "(60.0005,10.006) linked to (60.0005,10.006)[street:0]",
        // wheelchair-accessible
        // TODO: there are some duplicates in here which looks like a bug.
        "(60,10.012) → (60.002,10.012) PEDESTRIAN_AND_BICYCLE ♿✅",
        "(60,10.012) → (60,10.006) PEDESTRIAN_AND_BICYCLE ♿✅",
        "(60.002,10.012) → (60,10.012) PEDESTRIAN_AND_BICYCLE ♿✅",
        "(60,10.006) → (60,10.012) PEDESTRIAN_AND_BICYCLE ♿✅",
        "(60.0005,10.006) → (60,10.006) PEDESTRIAN_AND_BICYCLE ♿✅",
        "(60.0005,10.006) → (60,10.006) PEDESTRIAN_AND_BICYCLE ♿✅",
        "(60,10.006) → (60.0005,10.006) PEDESTRIAN_AND_BICYCLE ♿✅",
        "(60,10.006) → (60.0005,10.006) PEDESTRIAN_AND_BICYCLE ♿✅",
        "(60,10.006) → (60,10) PEDESTRIAN_AND_BICYCLE ♿✅",
        "(60,10) → (60,10.006) PEDESTRIAN_AND_BICYCLE ♿✅",
        "(60.002,10) → (60.002,10.012) PEDESTRIAN_AND_BICYCLE ♿✅",
        "(60.002,10) → (60,10) PEDESTRIAN_AND_BICYCLE ♿✅",
        "(60.002,10.012) → (60.002,10) PEDESTRIAN_AND_BICYCLE ♿✅",
        "(60,10) → (60.002,10) PEDESTRIAN_AND_BICYCLE ♿✅"
      );
  }
}
