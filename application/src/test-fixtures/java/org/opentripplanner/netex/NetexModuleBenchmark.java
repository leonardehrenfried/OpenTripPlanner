package org.opentripplanner.netex;

import java.io.File;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.regex.Pattern;
import org.opentripplanner.datastore.api.FileType;
import org.opentripplanner.datastore.file.ZipFileDataSource;
import org.opentripplanner.framework.io.HttpHeaders;
import org.opentripplanner.framework.io.OtpHttpClientFactory;
import org.opentripplanner.graph_builder.issue.service.DefaultDataImportIssueStore;
import org.opentripplanner.graph_builder.model.ConfiguredCompositeDataSource;
import org.opentripplanner.netex.config.NetexFeedParameters;
import org.opentripplanner.netex.configure.NetexConfigure;
import org.opentripplanner.service.streetdetails.internal.DefaultStreetDetailsRepository;
import org.opentripplanner.service.vehicleparking.internal.DefaultVehicleParkingRepository;
import org.opentripplanner.standalone.config.BuildConfig;
import org.opentripplanner.street.graph.Graph;
import org.opentripplanner.transit.dataimport.TimetableDataImport;
import org.opentripplanner.transit.model.framework.Deduplicator;
import org.opentripplanner.transit.service.SiteRepository;
import org.opentripplanner.transit.service.TransitRepository;
import org.opentripplanner.utils.time.DurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Standalone (non-JUnit) benchmark for {@link NetexModule}, run manually - it downloads the
 * Norwegian national NeTEx feed (including the stop register) and times importing it. NeTEx import is
 * CPU/memory sensitive on production-sized feeds, and that only shows up on real-sized data - the
 * unit tests only exercise small fixtures.
 * <p>
 * Run with (bump -Xmx as needed for the chosen feed):
 * <pre>
 * MAVEN_OPTS="-Xmx16g" mvn --projects application exec:java \
 *   -Dexec.mainClass="org.opentripplanner.netex.NetexModuleBenchmark" \
 *   -Dexec.classpathScope=test
 * </pre>
 * The feed is downloaded once into {@code -Dbenchmark.dataDir} (default:
 * {@code $TMPDIR/otp-benchmark}) and reused on subsequent runs. Override the feed with
 * {@code -Dbenchmark.netexUrl=<url>} (it must use the Entur file layout) and the number of timed
 * runs with {@code -Dbenchmark.iterations=<n>} (default 1).
 */
public class NetexModuleBenchmark {

  private static final Logger LOG = LoggerFactory.getLogger(NetexModuleBenchmark.class);

  private static final String DEFAULT_NETEX_URL =
    "https://storage.googleapis.com/marduk-production/outbound/netex/rb_norway-aggregated-netex.zip";

  private static final Duration DOWNLOAD_TIMEOUT = Duration.ofMinutes(5);

  /** The file layout used by Entur, see the Entur OTP deployment configuration. */
  private static final NetexFeedParameters ENTUR_FEED_PARAMETERS = NetexFeedParameters.of()
    .withFeedId("RB")
    .withSharedFilePattern(Pattern.compile("_stops\\.xml"))
    .withSharedGroupFilePattern(Pattern.compile("_(\\w{3})(_flexible)?_shared_data\\.xml"))
    .withGroupFilePattern(Pattern.compile("(\\w{3})_.*\\.xml"))
    .build();

  public static void main(String[] args) throws Exception {
    Path dataDir = Path.of(
      System.getProperty(
        "benchmark.dataDir",
        System.getProperty("java.io.tmpdir", "/tmp") + "/otp-benchmark"
      )
    );
    File inputFile = downloadIfMissing(
      System.getProperty("benchmark.netexUrl", DEFAULT_NETEX_URL),
      dataDir
    );
    int iterations = Integer.getInteger("benchmark.iterations", 1);

    for (int i = 1; i <= iterations; i++) {
      System.gc();
      long heapBefore = usedHeapBytes();

      System.out.printf("Running NeTEx import %d/%d on %s ...%n", i, iterations, inputFile);
      var result = runNetexModule(inputFile);

      long heapAfterImport = usedHeapBytes();
      System.gc();
      long heapAfterGc = usedHeapBytes();

      System.out.println();
      System.out.printf("==== Results %d/%d ====%n", i, iterations);
      System.out.printf("NeTEx module: %8s%n", DurationUtils.durationToStr(result.duration()));
      System.out.printf(
        "Heap: %,d MB (before) -> %,d MB (right after, pre-GC) -> %,d MB (after forced GC)%n",
        heapBefore / 1_000_000,
        heapAfterImport / 1_000_000,
        heapAfterGc / 1_000_000
      );
      System.out.printf(
        "Imported: %,d regular stops, %,d stations, %,d trip patterns, %,d trips, %,d issues%n",
        result.siteRepository().listRegularStops().size(),
        result.siteRepository().listStations().size(),
        result.timetableDataImport().getAllTripPatterns().size(),
        result
          .timetableDataImport()
          .getAllTripPatterns()
          .stream()
          .mapToLong(p -> p.scheduledTripsAsStream().count())
          .sum(),
        result.issueCount()
      );
      System.out.println();
    }
  }

  private static Result runNetexModule(File inputFile) {
    var transitRepository = new TransitRepository(new SiteRepository());
    var timetableDataImport = new TimetableDataImport();
    var issueStore = new DefaultDataImportIssueStore();

    var netexModule = new NetexConfigure(BuildConfig.DEFAULT).createNetexModule(
      List.of(
        new ConfiguredCompositeDataSource<>(
          new ZipFileDataSource(inputFile, FileType.NETEX),
          ENTUR_FEED_PARAMETERS
        )
      ),
      transitRepository,
      timetableDataImport,
      new DefaultVehicleParkingRepository(),
      new DefaultStreetDetailsRepository(),
      new Graph(),
      new Deduplicator(),
      issueStore
    );

    Instant start = Instant.now();
    netexModule.buildGraph();
    Duration duration = Duration.between(start, Instant.now());

    return new Result(
      duration,
      // the import merges into a new site repository, so it must be fetched after the import
      transitRepository.getSiteRepository(),
      timetableDataImport,
      issueStore.listIssues().size()
    );
  }

  private record Result(
    Duration duration,
    SiteRepository siteRepository,
    TimetableDataImport timetableDataImport,
    int issueCount
  ) {}

  private static long usedHeapBytes() {
    return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
  }

  private static File downloadIfMissing(String url, Path dataDir) throws Exception {
    Files.createDirectories(dataDir);
    String fileName = url.substring(url.lastIndexOf('/') + 1);
    Path target = dataDir.resolve(fileName);

    if (Files.exists(target) && Files.size(target) > 0) {
      System.out.printf(
        "Using cached file at %s (%,d MB)%n",
        target,
        Files.size(target) / 1_000_000
      );
      return target.toFile();
    }

    System.out.println("Downloading " + url + " to " + target + " ...");
    Path tmp = dataDir.resolve(fileName + ".part");
    try (var clientFactory = new OtpHttpClientFactory()) {
      var client = clientFactory.create(LOG);
      try (
        InputStream in = client.getAsInputStream(
          URI.create(url),
          DOWNLOAD_TIMEOUT,
          HttpHeaders.empty()
        )
      ) {
        Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
      }
    }
    Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING);
    System.out.printf("Download complete: %,d MB%n", Files.size(target) / 1_000_000);
    return target.toFile();
  }
}
