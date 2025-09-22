package org.opentripplanner.netex.mapping.calendar;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import org.rutebanken.netex.model.DayOfWeekEnumeration;

/**
 * Map between NeTEx {@link DayOfWeekEnumeration} to Java {@link DayOfWeek}. The NeTEx version have
 * "collection" type elements like WEEKDAYS, WEEKEND, EVERYDAY and NONE. Beacuse of this, the
 * mapping is not ono-to-one, but rather one-to-many.
 */
class DayOfWeekMapper {

  /** Utility class with static methods, prevent instantiation with private constructor */
  private DayOfWeekMapper() {}

  /**
   * Return a set Java DayOfWeek representing a union of all input values given. Each value is
   * mapped to a set of Java DayOfWeek, which is merged into one set.
   * <p>
   * [MONDAY, SATURDAY, WEEKEND] => [MONDAY, SATURDAY, SUNDAY]
   */
  static Set<DayOfWeek> mapDayOfWeeks(Collection<DayOfWeekEnumeration> values) {
    EnumSet<DayOfWeek> result = EnumSet.noneOf(DayOfWeek.class);
    for (DayOfWeekEnumeration it : values) {
      result.addAll(mapDayOfWeek(it));
    }
    return result;
  }

  /**
   * Maps given {@code value} into a set of Java DayOfWeek.
   * <ul>
   * <li>MONDAY to SUNDAY is mapped to a Set with one element
   * <li>NONE is mapped to an empty set
   * <li>WEEKDAYS is mapped to a set of MONDAY..FRIDAY
   * <li>WEEKEND is mapped to a set of SATURDAY..SUNDAY
   * <li>EVERYDAY is mapped to a set of MONDAY..SUNDAY
   * </ul>
   */
  static Set<DayOfWeek> mapDayOfWeek(DayOfWeekEnumeration value) {
    return switch (value) {
      case MONDAY -> EnumSet.of(DayOfWeek.MONDAY);
      case TUESDAY -> EnumSet.of(DayOfWeek.TUESDAY);
      case WEDNESDAY -> EnumSet.of(DayOfWeek.WEDNESDAY);
      case THURSDAY -> EnumSet.of(DayOfWeek.THURSDAY);
      case FRIDAY -> EnumSet.of(DayOfWeek.FRIDAY);
      case SATURDAY -> EnumSet.of(DayOfWeek.SATURDAY);
      case SUNDAY -> EnumSet.of(DayOfWeek.SUNDAY);
      case WEEKDAYS -> EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
      case WEEKEND -> EnumSet.range(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
      case EVERYDAY -> EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.SUNDAY);
      case NONE -> EnumSet.noneOf(DayOfWeek.class);
    };
  }
}
