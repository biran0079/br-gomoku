package common.boardclass.threatbased;

import common.boardclass.BoardClass;
import common.boardclass.testing.BoardClassUtil;
import common.pattern.Threat;
import model.GameBoard;

/**
* Factory for BoardClassWithMatchingThreats.
*/
public class FactoryForBoardWithThreat implements BoardClass.Factory<Threat> {

  private static final BoardClassWithMatchingThreats EMPTY_BOARD =
      new BoardClassWithMatchingThreats(BoardClassUtil.EMPTY_GAME_BOARD);

  @Override
  public BoardClass<Threat> fromGameBoard(GameBoard gameBoard) {
    if (gameBoard instanceof BoardClassWithMatchingThreats) {
      return (BoardClassWithMatchingThreats) gameBoard;
    }
    return new BoardClassWithMatchingThreats(gameBoard);
  }

  @Override
  public BoardClass<Threat> getEmptyBoard() {
    return EMPTY_BOARD;
  }
}
