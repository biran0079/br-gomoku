package common.boardclass;

import common.PatternType;
import common.PositionTransformer;
import common.StoneType;
import common.pattern.Pattern;

import model.GameBoard;

/**
 * Interface for board class that is used by AI searching algorithm.
 */
public interface BoardClass extends GameBoard {

  @Override
  BoardClass withPositionSet(int i, int j, StoneType stoneType);

  boolean matchesAny(StoneType stoneType, PatternType patternType);

  Iterable<Pattern> getMatchingPatterns(StoneType stoneType, PatternType patternType);

  BitBoard getBoard(PositionTransformer transformer);

  interface Factory extends GameBoard.Factory {

    BoardClass fromGameBoard(GameBoard gameBoard);

    @Override
    BoardClass getEmptyBoard();
  }
}
