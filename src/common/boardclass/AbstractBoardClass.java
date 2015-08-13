package common.boardclass;

import static common.PositionTransformer.CLOCK_180;
import static common.PositionTransformer.CLOCK_180_M;
import static common.PositionTransformer.CLOCK_270;
import static common.PositionTransformer.CLOCK_270_M;
import static common.PositionTransformer.CLOCK_90;
import static common.PositionTransformer.CLOCK_90_M;
import static common.PositionTransformer.IDENTITY;
import static common.PositionTransformer.IDENTITY_M;
import static common.PositionTransformer.LEFT_DIAGONAL;
import static common.PositionTransformer.RIGHT_DIAGONAL;

import common.Constants;
import common.PatternType;
import common.PositionTransformer;
import common.StoneType;

import java.util.EnumMap;
import java.util.Map;

import model.GameBoard;

/**
 * Shared logic for all BoardClass implementations.
 */
abstract class AbstractBoardClass implements BoardClass {

  private static final PositionTransformer[] TRACKING_TRANSFORMERS =
      new PositionTransformer[] {
          IDENTITY,
          IDENTITY_M,
          CLOCK_90,
          CLOCK_90_M,
          CLOCK_180,
          CLOCK_180_M,
          CLOCK_270,
          CLOCK_270_M,
          RIGHT_DIAGONAL,
          LEFT_DIAGONAL,
      };

  private final Map<PositionTransformer, BitBoard> map;

  AbstractBoardClass() {
    map = new EnumMap(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      map.put(transformer, BitBoard.emptyBoard(transformer.getBoardRowNumber()));
    }
  }

  AbstractBoardClass(AbstractBoardClass boardClass, int i, int j, StoneType stoneType) {
    map = new EnumMap(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      int ti = transformer.getI(i, j);
      int tj = transformer.getJ(i, j);
      map.put(transformer, boardClass.map.get(transformer).set(ti, tj, stoneType));
    }
  }

  AbstractBoardClass(GameBoard gameBoard) {
    map = new EnumMap(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      map.put(transformer, BitBoard.fromGameBoard(gameBoard, transformer));
    }
  }

  @Override
  public BitBoard getBoard(PositionTransformer transformer) {
    return map.get(transformer);
  }

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
