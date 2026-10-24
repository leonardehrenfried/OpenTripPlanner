package org.opentripplanner.netex.loader;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.xml.bind.JAXBException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.opentripplanner.datastore.api.DataSource;
import org.opentripplanner.datastore.api.FileType;
import org.opentripplanner.datastore.base.ByteArrayDataSource;

class NetexXmlDocumentPrefetcherTest {

  private static final DataSource A = document("a.xml", "A");
  private static final DataSource B = document("b.xml", "B");
  private static final DataSource C = document("c.xml", "C");

  @Test
  void parseInOrder() throws JAXBException {
    try (var subject = new NetexXmlDocumentPrefetcher(List.of(A, B, C), 2)) {
      assertThat(subject.next(A).getParticipantRef()).isEqualTo("A");
      assertThat(subject.next(B).getParticipantRef()).isEqualTo("B");
      assertThat(subject.next(C).getParticipantRef()).isEqualTo("C");
    }
  }

  @Test
  void rejectEntryRequestedOutOfOrder() {
    try (var subject = new NetexXmlDocumentPrefetcher(List.of(A, B), 1)) {
      assertThrows(IllegalStateException.class, () -> subject.next(B));
    }
  }

  @Test
  void rejectEntryAfterLastEntry() throws JAXBException {
    try (var subject = new NetexXmlDocumentPrefetcher(List.of(A), 1)) {
      subject.next(A);
      assertThrows(IllegalStateException.class, () -> subject.next(A));
    }
  }

  @Test
  void propagateParseError() {
    var invalid = source("invalid.xml", "<not-netex");
    try (var subject = new NetexXmlDocumentPrefetcher(List.of(invalid), 1)) {
      assertThrows(JAXBException.class, () -> subject.next(invalid));
    }
  }

  @Test
  void rejectParallelismLessThanOne() {
    assertThrows(IllegalArgumentException.class, () ->
      new NetexXmlDocumentPrefetcher(List.of(A), 0)
    );
  }

  private static DataSource document(String name, String participantRef) {
    return source(
      name,
      """
      <PublicationDelivery xmlns="http://www.netex.org.uk/netex" version="1.0">
        <PublicationTimestamp>2026-01-01T00:00:00</PublicationTimestamp>
        <ParticipantRef>%s</ParticipantRef>
      </PublicationDelivery>
      """.formatted(participantRef)
    );
  }

  private static DataSource source(String name, String content) {
    return new ByteArrayDataSource(name, name, FileType.NETEX, 0, 0, false).withBytes(
      content.getBytes(StandardCharsets.UTF_8)
    );
  }
}
