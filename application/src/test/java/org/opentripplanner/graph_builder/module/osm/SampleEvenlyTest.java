package org.opentripplanner.graph_builder.module.osm;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link WalkableAreaBuilder#sampleEvenly}, the uniform down-sampling used to bound
 * the O(n²) visibility test for very complex areas.
 */
class SampleEvenlyTest {

  private static List<Integer> range(int n) {
    return IntStream.range(0, n).boxed().toList();
  }

  @Test
  void returnsAllWhenNotLargerThanMax() {
    assertThat(WalkableAreaBuilder.sampleEvenly(range(5), 5))
      .containsExactly(0, 1, 2, 3, 4)
      .inOrder();
    assertThat(WalkableAreaBuilder.sampleEvenly(range(3), 10)).containsExactly(0, 1, 2).inOrder();
    assertThat(WalkableAreaBuilder.sampleEvenly(List.of(), 4)).isEmpty();
  }

  @Test
  void samplesEvenlyWhenLargerThanMax() {
    // 10 items down to 5: keeps every other one.
    assertThat(WalkableAreaBuilder.sampleEvenly(range(10), 5))
      .containsExactly(1, 3, 5, 7, 9)
      .inOrder();
  }

  @Test
  void neverExceedsMaxAndKeepsInputOrder() {
    for (int size = 1; size <= 200; size++) {
      for (int max = 1; max <= size; max++) {
        List<Integer> sampled = WalkableAreaBuilder.sampleEvenly(range(size), max);
        assertThat(sampled.size()).isAtMost(max);
        // strictly increasing => original order preserved and no duplicates
        for (int i = 1; i < sampled.size(); i++) {
          assertThat(sampled.get(i)).isGreaterThan(sampled.get(i - 1));
        }
      }
    }
  }
}
