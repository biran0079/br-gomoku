package common.pattern;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import common.PatternType;
import common.StoneType;
import common.boardclass.BoardClass;

import model.Position;

/**
 * Abstract interface for Pattern.
 */
public interface Pattern {

  ImmutableList<Position> getDefensiveMoves();

  boolean matches(BoardClass boardClass);

  StoneType getStoneType();

  interface Factory {

    ImmutableSet<Pattern> get(StoneType stoneType, PatternType patternType);
  }

  Factory DEFAULT_FACTORY = new Patterns();
}
