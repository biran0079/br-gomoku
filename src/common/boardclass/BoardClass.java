package common.boardclass;

import common.PatternType;
import common.PositionTransformer;
import common.StoneType;
import common.pattern.Pattern;

import model.GameBoard;

/**
 * Interface for a equivalent class of game board.
 */
public interface BoardClass extends GameBoard {

  boolean matchesAny(StoneType stoneType, PatternType patternType);

  Iterable<Pattern> getMatchingPatterns(StoneType stoneType, PatternType patternType);

  @Override
  BoardClass withPositionSet(int i, int j, StoneType stoneType);

  BitBoard getBoard(PositionTransformer transformer);

  interface Factory<T extends GameBoard> extends GameBoard.Factory {

    T fromGameBoard(GameBoard gameBoard);

    @Override
    T getEmptyBoard();
  }
}
