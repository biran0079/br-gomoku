package common;

import static common.Constants.BOARD_SIZE;

/**
 * Transform position into a new position.
 */
public enum PositionTransformer {

  IDENTITY(IndexTransformer.GET_I, IndexTransformer.GET_J, BOARD_SIZE),
  IDENTITY_M(IndexTransformer.GET_J, IndexTransformer.GET_I, BOARD_SIZE),
  CLOCK_90(IndexTransformer.GET_J, IndexTransformer.GET_C_I, BOARD_SIZE),
  CLOCK_90_M(IndexTransformer.GET_C_I, IndexTransformer.GET_J, BOARD_SIZE),
  CLOCK_180(IndexTransformer.GET_C_I, IndexTransformer.GET_C_J, BOARD_SIZE),
  CLOCK_180_M(IndexTransformer.GET_C_J, IndexTransformer.GET_I, BOARD_SIZE),
  CLOCK_270(IndexTransformer.GET_C_J, IndexTransformer.GET_I, BOARD_SIZE),
  CLOCK_270_M(IndexTransformer.GET_I, IndexTransformer.GET_C_J, BOARD_SIZE),

  RIGHT_DIAGONAL(IndexTransformer.SUM_I_J, IndexTransformer.GET_J, BOARD_SIZE + BOARD_SIZE - 1),
  RIGHT_DIAGONAL_REVERSE((i, j) -> i - j, (i, j) -> j, BOARD_SIZE + BOARD_SIZE - 1),

  LEFT_DIAGONAL(IndexTransformer.SUM_I_C_J, IndexTransformer.GET_I, BOARD_SIZE + BOARD_SIZE - 1),
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

  public int getBoardRowNumber() {
    return boardRowNumber;
  }

  public int getI(int i, int j) {
    return rowTransformer.get(i, j);
  }

  public int getJ(int i, int j) {
    return colTransformer.get(i, j);
  }
}
