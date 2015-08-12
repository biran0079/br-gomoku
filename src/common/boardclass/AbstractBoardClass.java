package common.boardclass;

import static common.PositionTransformer.IDENTITY;

import common.Constants;
import common.PatternType;
import common.PositionTransformer;
import common.StoneType;

/**
 * Shared logic for all BoardClass implementations.
 */
abstract class AbstractBoardClass implements BoardClass {

  @Override
  public boolean isEmpty() {
    return getStoneCount() == 0;
  }

  @Override
  public String toString() {
    return getBoard(PositionTransformer.IDENTITY).toString();
  }

  @Override
  public boolean isFull() {
    return getStoneCount() == Constants.BOARD_SIZE * Constants.BOARD_SIZE;
  }

  @Override
  public boolean wins(StoneType stoneType) {
    return matchesAny(stoneType, PatternType.FIVE);
  }

  @Override
  public int getStoneCount() {
    return getBoard(IDENTITY).getStoneCount();
  }

  @Override
  public StoneType get(int i, int j) {
    return getBoard(IDENTITY).get(i, j);
  }
}
