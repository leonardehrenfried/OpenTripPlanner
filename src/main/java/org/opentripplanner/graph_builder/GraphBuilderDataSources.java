package org.opentripplanner.graph_builder;

import org.opentripplanner.datastore.CompositeDataSource;
import org.opentripplanner.datastore.DataSource;
import org.opentripplanner.datastore.FileType;

import java.io.File;

/**
 * This is an abstraction over being able to configure a single directory for all graph-building related files and
 * defining individual files for tests.
 */
public interface GraphBuilderDataSources {
    boolean has(FileType type);
    Iterable<DataSource> get(FileType type);
    CompositeDataSource getBuildReportDir();
    File getCacheDirectory();
}
