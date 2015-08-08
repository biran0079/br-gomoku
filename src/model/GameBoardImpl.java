package model;

import common.Constants;
import common.Square;

import javax.inject.Inject;

/**
 * Implementation of game board.
 */
class GameBoardImpl implements GameBoard {

  private final Square[][] board;

  @Inject
  GameBoardImpl() {
    board = new Square[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
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
    Square[][] res = new Square[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
    for (int i = 0; i < Constants.BOARD_SIZE; i++)
      for (int j = 0; j < Constants.BOARD_SIZE; j++)
        res[i][j] = board[i][j];
    return res;
  }

  @Override
  public void initialize() {
    for (int i = 0; i < Constants.BOARD_SIZE; i++)
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        board[i][j] = Square.NOTHING;
      }
  }

  @Override
  public boolean isFull() {
    for (Square[] row : board) {
      for (Square square : row) {
        if (square == Square.NOTHING) {
          return false;
        }
      }
    }
    return true;
  }
}
