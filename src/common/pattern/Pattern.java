package common.pattern;

import com.google.common.collect.ImmutableSet;

import common.PositionTransformer;
import common.StoneType;

import model.Position;

/**
 * Abstract interface for Pattern.
 */
public interface Pattern {

  ImmutableSet<Position> getDefensiveMoves();

  StoneType getStoneType();

  interface Factory<T extends Pattern> {

    T create(int i, int j, int pattern, int mask,
             PositionTransformer transformer,
             StoneType stoneType,
             MoveType[] movePattern);
  }
}
