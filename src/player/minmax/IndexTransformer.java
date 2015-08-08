package player.minmax;

import common.Constants;

/**
 * Transforms board index into a different value.
 */
interface IndexTransformer {

  IndexTransformer GET_I = (i, j) -> i;
  IndexTransformer GET_J = (i, j) -> j;
  IndexTransformer GET_C_I = (i, j) -> Constants.BOARD_SIZE - i - 1;
  IndexTransformer GET_C_J = (i, j) -> Constants.BOARD_SIZE - j - 1;

  IndexTransformer SUM_I_J = (i, j) -> i + j;
  IndexTransformer SUM_I_C_J = (i, j) -> i + Constants.BOARD_SIZE - j - 1;

  int get(int i, int j);
}
