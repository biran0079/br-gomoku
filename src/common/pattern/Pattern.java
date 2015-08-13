package common.pattern;

import com.google.common.collect.ImmutableList;

import common.boardclass.BoardClass;

import model.Position;

/**
 * Abstract interface for Pattern.
 */
public interface Pattern {

  ImmutableList<Position> getDefensiveMoves();

  boolean matches(BoardClass boardClass);
}
