package org.opentripplanner.graph_builder.module.osm.storage;

import gnu.trove.set.TLongSet;
import java.util.Arrays;

/**
 * An immutable set of longs backed by a sorted array, optimized for lookups in (mostly) ascending
 * order: OSM PBF files are normally sorted by id, so consecutive lookups only need to move a cursor
 * forward a little, touching memory sequentially instead of probing a hash table at random.
 * Lookups out of order fall back to a binary search. Not thread-safe.
 */
final class SortedLongSet {

  private final long[] values;
  private int cursor = 0;

  private SortedLongSet(long[] values) {
    this.values = values;
  }

  static SortedLongSet union(TLongSet a, TLongSet b) {
    long[] all = new long[a.size() + b.size()];
    System.arraycopy(a.toArray(), 0, all, 0, a.size());
    System.arraycopy(b.toArray(), 0, all, a.size(), b.size());
    Arrays.parallelSort(all);
    int n = 0;
    for (int i = 0; i < all.length; i++) {
      if (n == 0 || all[i] != all[n - 1]) {
        all[n++] = all[i];
      }
    }
    return new SortedLongSet(Arrays.copyOf(all, n));
  }

  int size() {
    return values.length;
  }

  boolean contains(long value) {
    if (values.length == 0) {
      return false;
    }
    if (cursor > 0 && values[cursor - 1] >= value) {
      // out of order - binary search what is behind the cursor
      int i = Arrays.binarySearch(values, 0, cursor, value);
      cursor = i >= 0 ? i : -i - 1;
      return i >= 0;
    }
    // exponential search forward from the cursor
    int lo = cursor;
    int step = 1;
    int hi = lo;
    while (hi < values.length && values[hi] < value) {
      lo = hi + 1;
      hi += step;
      step <<= 1;
    }
    int i = Arrays.binarySearch(values, lo, Math.min(hi + 1, values.length), value);
    cursor = i >= 0 ? i : -i - 1;
    return i >= 0;
  }
}
