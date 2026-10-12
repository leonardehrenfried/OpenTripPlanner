package org.opentripplanner.street.model.path;

import static com.google.common.truth.Truth.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class EmptyStreetPathTest {

  @Test
  void hasNoEdgesAndNoCost() {
    var path = StreetPath.EMPTY;

    assertThat(path.isEmpty()).isTrue();
    assertThat(path.legs(0)).isEmpty();
    assertThat(path.geometry().isEmpty()).isTrue();
    assertThat(path.duration()).isEqualTo(Duration.ZERO);
    assertThat(path.generalizedCost()).isEqualTo(0);
    assertThat(path.traversalDistance()).isEqualTo(0);
  }

  /// Code checks for the empty path by identity, so it must stay a singleton when it is
  /// deserialized as part of the graph.
  @Test
  void staysSingletonWhenDeserialized() throws Exception {
    var bytes = new ByteArrayOutputStream();
    try (var out = new ObjectOutputStream(bytes)) {
      out.writeObject(StreetPath.EMPTY);
    }
    try (var in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
      assertThat(in.readObject()).isSameInstanceAs(StreetPath.EMPTY);
    }
  }
}
