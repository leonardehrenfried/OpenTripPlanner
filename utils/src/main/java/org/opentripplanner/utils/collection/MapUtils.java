package org.opentripplanner.utils.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapUtils {

  /**
   * Map a collection of objects of type <em>S</em> to a list of type <em>T</em> using the provided
   * mapping function.
   * <p>
   * Nullsafe: if <em>entities</em> is <code>null</code>, then <code>null</code> is returned.
   */
  public static <S, T> List<T> mapToList(Collection<S> entities, Function<S, T> mapper) {
    return entities == null ? null : entities.stream().map(mapper).collect(Collectors.toList());
  }

  /**
   * Takes a list of maps and returns the union of all of them.
   * <p>
   * If there are duplicate keys then the one from the later argument overwrites the value in an
   * earlier argument.
   */
  @SafeVarargs
  public static <K, V> Map<K, V> combine(Map<K, V>... maps) {
    var ret = new HashMap<K, V>();
    Arrays.stream(maps).forEach(ret::putAll);
    return Map.copyOf(ret);
  }

  /**
   * Transforms the keys of a map using the provided key mapper function.
   */
  public static <K, V> Map<K, V> transformKeys(Map<K, V> input, Function<K, K> keyMapper) {
    if (input.isEmpty()) {
      return Map.of();
    }
    // newHashMap sizes the backing array for exactly n entries, avoiding a rehash
    var ret = HashMap.<K, V>newHashMap(input.size());
    for (var entry : input.entrySet()) {
      ret.put(keyMapper.apply(entry.getKey()), entry.getValue());
    }
    return Map.copyOf(ret);
  }
}
