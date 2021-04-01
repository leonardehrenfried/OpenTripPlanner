package org.opentripplanner.routing.street;

import org.apache.commons.lang3.ArrayUtils;
import org.opentripplanner.datastore.CompositeDataSource;
import org.opentripplanner.datastore.DataSource;
import org.opentripplanner.datastore.FileType;
import org.opentripplanner.datastore.file.FileDataSource;
import org.opentripplanner.graph_builder.GraphBuilder;
import org.opentripplanner.graph_builder.GraphBuilderDataSources;
import org.opentripplanner.graph_builder.module.osm.DefaultWayPropertySetSource;
import org.opentripplanner.graph_builder.module.osm.OpenStreetMapModule;
import org.opentripplanner.openstreetmap.BinaryOpenStreetMapProvider;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.standalone.config.BuildConfig;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestGraphBuilder {

    public static class TestDataSources implements GraphBuilderDataSources {

        private final List<String> osmFiles;

        public TestDataSources(List<String> osmFiles) {
            this.osmFiles = osmFiles;
        }

        @Override
        public boolean has(FileType type) {
            return  type == FileType.OSM;
        }

        @Override
        public Iterable<DataSource> get(FileType type) {
            return osmFiles.stream().map(f -> {
                var file = new File(f);
                if(!file.exists()) {
                    throw new RuntimeException("Cannot find file "+ f);
                }
                return new FileDataSource(file, FileType.OSM);
            }).collect(Collectors.toList());
        }

        @Override
        public CompositeDataSource getBuildReportDir() {
            return null;
        }

        @Override
        public File getCacheDirectory() {
            return null;
        }
    }

    static Graph buildOsmGraph(String... osmFile) {
        return buildGraph(osmFile, new String[]{});
    }

    private static Graph buildGraph(String[] osmFiles, String[] gtfsFiles) {
        List<BinaryOpenStreetMapProvider> osmProviders = Arrays.stream(osmFiles)
                .map(f -> new BinaryOpenStreetMapProvider(new File(f), true))
                .collect(Collectors.toList());

        OpenStreetMapModule osmModule = new OpenStreetMapModule(osmProviders);
        osmModule.skipVisibility = true;
        osmModule.staticBikeParkAndRide = true;
        osmModule.setDefaultWayPropertySetSource(new DefaultWayPropertySetSource());

        var config = BuildConfig.DEFAULT;
        var datasource = new TestDataSources(Arrays.asList(osmFiles));
        GraphBuilder graphBuilder = GraphBuilder.create(config, datasource, null);

        graphBuilder.run();
        Graph graph = graphBuilder.getGraph();
        assert(graph != null);
        assert(!graph.getStreetEdges().isEmpty());

        return graph;
    }
}
