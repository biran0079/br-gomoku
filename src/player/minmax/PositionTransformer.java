package player.minmax;

import common.Constants;

import static common.Constants.BOARD_SIZE;

import static player.minmax.IndexTransformer.*;
import static player.minmax.IndexTransformer.SUM_I_C_J;

/**
 * Transform position into a new position.
 */
public enum PositionTransformer {

  IDENTITY(GET_I, GET_J, BOARD_SIZE),
  IDENTITY_M(GET_J, GET_I, BOARD_SIZE),
  CLOCK_90(GET_J, GET_C_I, BOARD_SIZE),
  CLOCK_90_M(GET_C_I, GET_J, BOARD_SIZE),
  CLOCK_180(GET_C_I, GET_C_J, BOARD_SIZE),
  CLOCK_180_M(GET_C_J, GET_I, BOARD_SIZE),
  CLOCK_270(GET_C_J, GET_I, BOARD_SIZE),
  CLOCK_270_M(GET_I, GET_C_J, BOARD_SIZE),

  RIGHT_DIAGONAL(SUM_I_J, GET_J, BOARD_SIZE + BOARD_SIZE - 1),
  RIGHT_DIAGONAL_REVERSE((i, j) -> i - j, (i, j) -> j, BOARD_SIZE + BOARD_SIZE - 1),

  LEFT_DIAGONAL(SUM_I_C_J, GET_I, BOARD_SIZE + BOARD_SIZE - 1),
  LEFT_DIAGONAL_REVERSE((i, j) -> j, (i, j) -> j - i + Constants.BOARD_SIZE - 1, BOARD_SIZE + BOARD_SIZE - 1);

  private final IndexTransformer rowTransformer;
  private final IndexTransformer colTransformer;
  private final int boardRowNumber;

  PositionTransformer(IndexTransformer rowTransformer,
                      IndexTransformer colTransformer,
                      int boardRowNumber) {
    this.rowTransformer = rowTransformer;
    this.colTransformer = colTransformer;
    this.boardRowNumber = boardRowNumber;
  }

  int getBoardRowNumber() {
    return boardRowNumber;
  }

  public int getI(int i, int j) {
    return rowTransformer.get(i, j);
  }

  public int getJ(int i, int j) {
    return colTransformer.get(i, j);
  }
}
