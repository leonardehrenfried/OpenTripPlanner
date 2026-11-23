package org.opentripplanner.street.model.path;

import java.util.List;
import org.opentripplanner.street.search.state.State;

/**
 * Gives tests access to the states of a {@link StreetPath}, which are deliberately not part of its
 * public API.
 */
public class StreetPathForTest {

  private StreetPathForTest() {}

  /**
   * The states of the path in chronological order.
   */
  public static List<State> states(StreetPath path) {
    return ((DefaultStreetPath) path).states();
  }

  /**
   * The states in chronological order of the path ending (for arriveBy searches: starting) in the
   * given state.
   */
  public static List<State> states(State finalState) {
    return states(path(finalState));
  }

  /**
   * The chronological path of the search ending in the given state, see
   * {@link DefaultStreetSearchResult#toPath()}.
   */
  public static DefaultStreetPath path(State finalState) {
    return (DefaultStreetPath) DefaultStreetSearchResult.of(finalState).toPath();
  }
}
