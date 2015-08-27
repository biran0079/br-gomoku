package common.pattern;

import com.google.common.collect.ImmutableSet;
import common.PositionTransformer;
import common.StoneType;
import common.boardclass.BoardClass;
import model.Position;

import java.util.Set;

/**
 * Abstract interface for Pattern.
 */
public interface Pattern {

  ImmutableSet<Position> getDefensiveMoves();

  StoneType getStoneType();

  boolean matches(BoardClass<?> boardClass);

  interface Factory<T extends Pattern> {

    T create(int i, int j, int pattern, int mask,
             PositionTransformer transformer,
             StoneType stoneType,
             MoveType[] movePattern);
  }

  interface Corpus<T extends Pattern> {

    ImmutableSet<T> get(StoneType stoneType, PatternType PatternType);

    Set<T> get(int i, int j, StoneType stoneType);

    default Set<T> get(Position p, StoneType stoneType) {
      return get(p.getRowIndex(), p.getColumnIndex(), stoneType);
    }
  }
}
