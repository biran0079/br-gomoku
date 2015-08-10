package model;

import common.BoardClass;
import common.Constants;
import common.StoneType;

import javax.inject.Inject;

/**
 * Implementation of game board.
 */
class GameBoardImpl implements GameBoard {

  private final StoneType[][] board;

  @Inject
  GameBoardImpl() {
    board = new StoneType[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
  }

  @Override
  public StoneType get(int i, int j) {
    return board[i][j];
  }

  @Override
  public void set(Position position, StoneType stoneType) {
    board[position.getRowIndex()][position.getColumnIndex()] = stoneType;
  }

  @Override
  public StoneType[][] toArray() {
    StoneType[][] res = new StoneType[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
    for (int i = 0; i < Constants.BOARD_SIZE; i++)
      for (int j = 0; j < Constants.BOARD_SIZE; j++)
        res[i][j] = board[i][j];
    return res;
  }

  @Override
  public void initialize() {
    for (int i = 0; i < Constants.BOARD_SIZE; i++)
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        board[i][j] = StoneType.NOTHING;
      }
  }

  @Override
  public boolean isFull() {
    for (StoneType[] row : board) {
      for (StoneType stoneType : row) {
        if (stoneType == StoneType.NOTHING) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean isEmpty() {
    for (StoneType[] row : board) {
      for (StoneType stoneType : row) {
        if (stoneType != StoneType.NOTHING) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public String toString() {
    return BoardClass.fromGameBoard(this).toString();
  }
}
