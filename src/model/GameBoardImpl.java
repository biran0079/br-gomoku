package model;

import common.Constants;
import common.Square;

/**
 * Implementation of game board.
 */
class GameBoardImpl implements GameBoard {

  private final Square[][] board;

  GameBoardImpl() {
    board = new Square[Constants.ROW_NUM][Constants.COL_NUM];
  }

  @Override
  public Square get(Position position) {
    return board[position.getRowIndex()][position.getColumnIndex()];
  }

  @Override
  public void set(Position position, Square square) {
    board[position.getRowIndex()][position.getColumnIndex()] = square;
  }

  @Override
  public Square[][] toArray() {
    Square[][] res = new Square[Constants.ROW_NUM][Constants.COL_NUM];
    for (int i = 0; i < Constants.ROW_NUM; i++)
      for (int j = 0; j < Constants.COL_NUM; j++)
        res[i][j] = board[i][j];
    return res;
  }

  @Override
  public void initialize() {
    for (int i = 0; i < Constants.ROW_NUM; i++)
      for (int j = 0; j < Constants.COL_NUM; j++) {
        board[i][j] = Square.NOTHING;
      }
  }
}
