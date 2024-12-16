package org.opentripplanner.utils.collection;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ImmutableEnumSetTest {

  @Test
  void contains(){
    var set = ImmutableEnumSet.of(DayOfWeek.class, MONDAY, WEDNESDAY);
    assertFalse(set.isEmpty());
    assertTrue(set.contains(MONDAY));
    assertFalse(set.contains(TUESDAY));
    assertTrue(set.contains(WEDNESDAY));
    assertFalse(set.contains(THURSDAY));
    assertFalse(set.contains(FRIDAY));
    assertFalse(set.contains(SATURDAY));
    assertFalse(set.contains(SUNDAY));
  }

  @Test
  void allOf(){
    var set = ImmutableEnumSet.allOff(DayOfWeek.class);
    assertFalse(set.isEmpty());
    assertTrue(set.contains(MONDAY));
    assertTrue(set.contains(TUESDAY));
    assertTrue(set.contains(WEDNESDAY));
    assertTrue(set.contains(THURSDAY));
    assertTrue(set.contains(FRIDAY));
    assertTrue(set.contains(SATURDAY));
    assertTrue(set.contains(SUNDAY));
  }

  @Test
  void empty(){
    assertFalse(ImmutableEnumSet.of(DayOfWeek.class, SUNDAY).isEmpty());
    assertTrue(ImmutableEnumSet.of(DayOfWeek.class).isEmpty());
  }

  @Test
  void string(){
    var set = ImmutableEnumSet.of(DayOfWeek.class, SUNDAY, MONDAY, THURSDAY);
    assertEquals("[MONDAY,THURSDAY,SUNDAY,]", set.toString());
  }

  @Test
  void values(){
    var set = ImmutableEnumSet.of(DayOfWeek.class, SUNDAY, THURSDAY, MONDAY).values();
    assertEquals(Set.of(SUNDAY, MONDAY, THURSDAY), set);
  }
}