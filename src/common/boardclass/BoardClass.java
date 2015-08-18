package common.boardclass;

import common.PositionTransformer;
import common.StoneType;
import common.pattern.Pattern;
import common.pattern.PatternType;

import model.GameBoard;

/**
 * Interface for a equivalent class of game board.
 */
public interface BoardClass<T extends Pattern> extends GameBoard {

  boolean matchesAny(StoneType stoneType, PatternType patternType);

  Iterable<? extends T> getMatchingPatterns(StoneType stoneType, PatternType patternType);

  @Override
  BoardClass<T> withPositionSet(int i, int j, StoneType stoneType);

  BitBoard getBoard(PositionTransformer transformer);

  interface Factory<T extends Pattern> extends GameBoard.Factory {

    BoardClass<T> fromGameBoard(GameBoard gameBoard);

    @Override
    BoardClass<T> getEmptyBoard();
  }
}
