package org.opentripplanner.netex.loader;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.opentripplanner.datastore.api.DataSource;
import org.opentripplanner.datastore.api.FileType;
import org.opentripplanner.datastore.file.DirectoryDataSource;
import org.opentripplanner.netex.index.NetexEntityIndex;

class NetexFileLoaderTest {

  private static final String SHARED_FILE = "_stops.xml";
  private static final String GROUP_SHARED_FILE = "_ABC_shared_data.xml";
  private static final String GROUP_FILE_1 = "ABC_line_1.xml";
  private static final String GROUP_FILE_2 = "ABC_line_2.xml";

  @TempDir
  private Path dir;

  @BeforeEach
  void setup() throws IOException {
    for (var name : List.of(SHARED_FILE, GROUP_SHARED_FILE, GROUP_FILE_1, GROUP_FILE_2)) {
      write(name, document(name));
    }
  }

  @Test
  void loadInOrder() throws JAXBException {
    var hierarchy = hierarchy();
    var group = hierarchy.groups().iterator().next();
    var rootIndex = new NetexEntityIndex();
    var groupIndex = rootIndex.push();

    try (var subject = loader(hierarchy, 2)) {
      assertThat(
        subject.nextSharedFile(entry(hierarchy, SHARED_FILE)).getParticipantRef()
      ).isEqualTo(SHARED_FILE);
      assertThat(
        subject.nextSharedFile(entry(group.sharedEntries(), GROUP_SHARED_FILE)).getParticipantRef()
      ).isEqualTo(GROUP_SHARED_FILE);

      subject.groupIndexReady(group, groupIndex);

      for (var entry : group.independentEntries()) {
        var loaded = subject.nextGroupFile(entry);
        assertThat(loaded.index().pop()).isSameInstanceAs(groupIndex);
        assertThat(loaded.issues()).isEmpty();
      }
    }
  }

  @Test
  void rejectGroupFileBeforeGroupIndexIsReady() throws JAXBException {
    var hierarchy = hierarchy();
    var group = hierarchy.groups().iterator().next();

    try (var subject = loader(hierarchy, 1)) {
      subject.nextSharedFile(entry(hierarchy, SHARED_FILE));
      subject.nextSharedFile(entry(group.sharedEntries(), GROUP_SHARED_FILE));

      var groupFile = group.independentEntries().iterator().next();
      assertThrows(IllegalStateException.class, () -> subject.nextGroupFile(groupFile));
    }
  }

  @Test
  void rejectEntryRequestedOutOfOrder() {
    var hierarchy = hierarchy();
    var group = hierarchy.groups().iterator().next();

    try (var subject = loader(hierarchy, 1)) {
      var groupSharedFile = entry(group.sharedEntries(), GROUP_SHARED_FILE);
      assertThrows(IllegalStateException.class, () -> subject.nextSharedFile(groupSharedFile));
    }
  }

  @Test
  void rejectSharedFileRequestedAsGroupFile() {
    var hierarchy = hierarchy();

    try (var subject = loader(hierarchy, 1)) {
      var sharedFile = entry(hierarchy, SHARED_FILE);
      assertThrows(IllegalStateException.class, () -> subject.nextGroupFile(sharedFile));
    }
  }

  @Test
  void propagateParseErrorOfSharedFile() throws IOException {
    write(SHARED_FILE, "<not-netex");
    var hierarchy = hierarchy();

    try (var subject = loader(hierarchy, 1)) {
      var sharedFile = entry(hierarchy, SHARED_FILE);
      assertThrows(JAXBException.class, () -> subject.nextSharedFile(sharedFile));
    }
  }

  @Test
  void propagateParseErrorOfGroupFile() throws IOException, JAXBException {
    write(GROUP_FILE_1, "<not-netex");
    write(GROUP_FILE_2, "<not-netex");
    var hierarchy = hierarchy();
    var group = hierarchy.groups().iterator().next();

    try (var subject = loader(hierarchy, 1)) {
      subject.nextSharedFile(entry(hierarchy, SHARED_FILE));
      subject.nextSharedFile(entry(group.sharedEntries(), GROUP_SHARED_FILE));
      subject.groupIndexReady(group, new NetexEntityIndex().push());

      var groupFile = group.independentEntries().iterator().next();
      assertThrows(JAXBException.class, () -> subject.nextGroupFile(groupFile));
    }
  }

  @Test
  void loadFileBiggerThanTheSizeLimit() throws JAXBException {
    var hierarchy = hierarchy();

    try (var subject = new NetexFileLoader(hierarchy, Set.of(), 1, 4, 1)) {
      assertThat(subject.nextSharedFile(entry(hierarchy, SHARED_FILE))).isNotNull();
    }
  }

  @Test
  void rejectInvalidParameters() {
    var hierarchy = hierarchy();
    assertThrows(IllegalArgumentException.class, () ->
      new NetexFileLoader(hierarchy, Set.of(), 0, 1, 1)
    );
    assertThrows(IllegalArgumentException.class, () ->
      new NetexFileLoader(hierarchy, Set.of(), 1, 0, 1)
    );
  }

  private NetexDataSourceHierarchy hierarchy() {
    return new NetexDataSourceHierarchy(
      new DirectoryDataSource(dir.toFile(), FileType.NETEX)
    ).prepare(
      Pattern.compile("$^"),
      Pattern.compile(SHARED_FILE),
      Pattern.compile("_(\\w{3})_shared_data\\.xml"),
      Pattern.compile("(\\w{3})_.*\\.xml")
    );
  }

  private static NetexFileLoader loader(NetexDataSourceHierarchy hierarchy, int maxFilesAhead) {
    return new NetexFileLoader(hierarchy, Set.of(), 2, maxFilesAhead, 1_000_000);
  }

  private static DataSource entry(NetexDataSourceHierarchy hierarchy, String name) {
    return entry(hierarchy.sharedEntries(), name);
  }

  private static DataSource entry(Iterable<DataSource> entries, String name) {
    for (var entry : entries) {
      if (entry.name().equals(name)) {
        return entry;
      }
    }
    throw new IllegalArgumentException("Entry not found: " + name);
  }

  private void write(String name, String content) throws IOException {
    Files.writeString(dir.resolve(name), content);
  }

  private static String document(String participantRef) {
    return """
    <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.0">
      <PublicationTimestamp>2026-01-01T00:00:00</PublicationTimestamp>
      <ParticipantRef>%s</ParticipantRef>
      <dataObjects/>
    </PublicationDelivery>
    """.formatted(participantRef);
  }
}
