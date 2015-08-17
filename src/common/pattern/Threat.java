package common.pattern;

import com.google.common.collect.ImmutableSet;
import common.PatternType;
import common.StoneType;
import model.Position;

/**
 * Interface for Threat that is used in threat based search.
 */
public interface Threat extends Pattern {

  Position getOffensiveMove();

  interface Factory {

    ImmutableSet<Threat> get(StoneType stoneType, PatternType patternType);
  }
}
