package org.opentripplanner.graph_builder.module.osm;

import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.opentripplanner.graph_builder.issue.api.DataImportIssueStore;
import org.opentripplanner.graph_builder.services.osm.CustomNamer;
import org.opentripplanner.openstreetmap.OpenStreetMapProvider;
import org.opentripplanner.openstreetmap.model.OSMLevel;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.street.model.edge.AreaEdge;
import org.opentripplanner.test.support.VariableSource;
import org.opentripplanner.transit.model.framework.Deduplicator;

public class WalkableAreaBuilderTest {

  private static final String LUND_STATION_OSM = "lund-station-sweden.osm.pbf";
  private static final int NODE_ID_ON_PLATFORM_6 = 1025307935;
  private final Deduplicator deduplicator = new Deduplicator();

  public Graph buildGraph(String osmFileName, boolean visibility) {
    final Graph graph = new Graph(deduplicator);
    final boolean platformEntriesLinking = true;
    final int maxAreaNodes = 5;

    final Set<String> boardingAreaRefTags = Set.of();
    final OSMDatabase osmdb = new OSMDatabase(DataImportIssueStore.NOOP, boardingAreaRefTags);
    final CustomNamer customNamer = null;

    final OpenStreetMapModule.Handler handler = new OpenStreetMapModule.Handler(
      graph,
      osmdb,
      DataImportIssueStore.NOOP,
      () -> false,
      () -> false,
      () -> false,
      () -> customNamer,
      () -> maxAreaNodes,
      false,
      boardingAreaRefTags,
      false,
      false,
      new HashMap<>()
    );

    final File file = new File(this.getClass().getResource(osmFileName).getFile());
    new OpenStreetMapProvider(file, true).readOSM(osmdb);
    osmdb.postLoad();

    final WalkableAreaBuilder walkableAreaBuilder = new WalkableAreaBuilder(
      graph,
      osmdb,
      handler,
      DataImportIssueStore.NOOP,
      maxAreaNodes,
      platformEntriesLinking,
      boardingAreaRefTags
    );

    final Map<Area, OSMLevel> areasLevels = osmdb
      .getWalkableAreas()
      .stream()
      .collect(toMap(a -> a, a -> osmdb.getLevelForWay(a.parent)));
    final List<AreaGroup> areaGroups = AreaGroup.groupAreas(areasLevels);

    final Consumer<AreaGroup> build = visibility
      ? walkableAreaBuilder::buildWithVisibility
      : walkableAreaBuilder::buildWithoutVisibility;

    areaGroups.forEach(build);
    return graph;
  }

  static Stream<Arguments> areaCases = Stream.of(
    Arguments.of(LUND_STATION_OSM, true, NODE_ID_ON_PLATFORM_6),
    Arguments.of(LUND_STATION_OSM, false, NODE_ID_ON_PLATFORM_6)
  );

  @ParameterizedTest(
    name = "should add areas on osm way containing node {2} from file {0} when visibility={1} "
  )
  @VariableSource("areaCases")
  public void testAreaCreation(String filename, boolean visibility, int nodeId) {
    var graph = buildGraph(filename, visibility);
    var areas = graph
      .getEdgesOfType(AreaEdge.class)
      .stream()
      .filter(a -> a.getToVertex().getLabel().equals("osm:node:" + nodeId))
      .map(AreaEdge::getArea)
      .distinct()
      .toList();
    assertEquals(1, areas.size());
    assertFalse(areas.get(0).getAreas().isEmpty());
  }
}
