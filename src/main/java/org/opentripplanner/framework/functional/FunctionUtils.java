package org.opentripplanner.framework.functional;

import java.util.Objects;
import java.util.function.Function;

public class FunctionUtils {

  @FunctionalInterface
  public interface TriFunction<A, B, C, R> {
    R apply(A a, B b, C c);
  }
}
