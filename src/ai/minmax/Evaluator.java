package ai.minmax;

import common.boardclass.BoardClass;

/**
 * Evaluates game board status into an int.
 * Larger value indicates black is more likely to win, smaller value indicates white is more likely to win.
 */
public interface Evaluator {

  int eval(BoardClass boardClass, boolean blackMoveNext);
}
