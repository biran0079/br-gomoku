package common.boardclass.basic;

import common.boardclass.BoardClass;
import common.boardclass.testing.BoardClassUtil;
import common.pattern.Pattern;

import model.GameBoard;

/**
 * Factory for BoardClassWithMatchingPatterns.
*/
public class FactoryForBoardWithPattern implements BoardClass.Factory<Pattern> {

  private static final BoardClassWithMatchingPatterns EMPTY_BOARD =
      new BoardClassWithMatchingPatterns(BoardClassUtil.EMPTY_GAME_BOARD);

  @Override
  public BoardClass<Pattern> fromGameBoard(GameBoard gameBoard) {
    if (gameBoard instanceof BoardClassWithMatchingPatterns) {
      return (BoardClassWithMatchingPatterns) gameBoard;
    }
    return new BoardClassWithMatchingPatterns(gameBoard);
  }

  @Override
  public BoardClass<Pattern> getEmptyBoard() {
    return EMPTY_BOARD;
  }
}
