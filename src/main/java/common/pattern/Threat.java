package common.pattern;

import com.google.common.collect.ImmutableSet;
import model.Position;

/**
 * A threat to make a winning pattern with a offensive move.
 */
public interface Threat extends Pattern {

  Position getOffensiveMove();

  boolean dependingOn(Threat threat);
}
