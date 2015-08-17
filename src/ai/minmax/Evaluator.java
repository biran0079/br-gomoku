package ai.minmax;

import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.Pattern;

/**
 * Evaluates game board status into an int.
 * Larger value indicates black is more likely to win, smaller value indicates white is more likely to win.
 */
public interface Evaluator {

  int eval(BoardClass boardClass, StoneType nextToMove);
}
