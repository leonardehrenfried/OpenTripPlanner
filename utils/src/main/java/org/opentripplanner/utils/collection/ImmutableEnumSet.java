package org.opentripplanner.utils.collection;

import java.util.HashSet;
import java.util.Set;
import org.opentripplanner.utils.lang.BitSetUtils;

public final class ImmutableEnumSet<T extends Enum<T>> {

  private final int bitset;
  private final Class<T> clazz;

  private ImmutableEnumSet(Class<T> clazz, int bitset) {
    this.bitset = bitset;
    this.clazz = clazz;
  }

  @SafeVarargs
  public static <E extends Enum<E>> ImmutableEnumSet<E> of(Class<E> clazz, E... elements)  {
    checkClass(clazz);

    int bitset = 0;
    for(E e : elements) {
      bitset = BitSetUtils.set(bitset, e.ordinal(), true);
    }
    return new ImmutableEnumSet<>(clazz, bitset);
  }
  public static <E extends Enum<E>> ImmutableEnumSet<E> allOff(Class<E> clazz) {
    return of(clazz, clazz.getEnumConstants());
  }

  private static <E extends Enum<E>> void checkClass(Class<E> clazz) {
    if(!clazz.isEnum()) {
      throw new IllegalArgumentException(String.format("Class %s is not an enum", clazz.getName()));
    }
  }

  public boolean isEmpty() {
    return bitset == 0;
  }

  public boolean contains(T value) {
    return BitSetUtils.get(bitset, value.ordinal());
  }

  public Set<T> values() {
    var set = new HashSet<T>();
    for(var e: clazz.getEnumConstants()){
      if(BitSetUtils.get(bitset, e.ordinal())) {
        set.add(e);
      }
    }
    return set;
  }

  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append("[");
    for(var e: clazz.getEnumConstants()){
      if(BitSetUtils.get(bitset, e.ordinal())) {
        builder.append(e.name());
        builder.append(",");
      }
    }
    builder.append("]");
    return builder.toString();
  }
}
