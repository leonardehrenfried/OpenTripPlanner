package org.opentripplanner.model.plan;

import java.util.Optional;
import javax.annotation.Nullable;
import org.opentripplanner.framework.i18n.I18NString;

/**
 * A set of stairs or steps that the passenger uses during a leg.
 * Note: this model is very small, almost useless at the moment but in the future we want to add
 * whether they are going up or down and the number of individual steps.
 */
public final class Stairs {

  private final I18NString name;

  public Stairs(@Nullable I18NString name) {
    this.name = name;
  }

  /**
   * Name of the stairs if it has one (most stairs don't, but some do).
   */
  public Optional<I18NString> name() {
    return Optional.ofNullable(name);
  }
}
