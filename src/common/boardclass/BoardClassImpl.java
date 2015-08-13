package common.boardclass;

import com.google.common.collect.Iterables;
import common.*;
import common.pattern.Pattern;

import model.GameBoard;

import java.util.EnumMap;
import java.util.Map;

import static common.PositionTransformer.*;

/**
 * Class of bit boards by PositionTransformer operation.
 */
class BoardClassImpl extends AbstractBoardClass {

  private static final Pattern.Factory PATTERNS = Pattern.DEFAULT_FACTORY;

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

  private static final BoardClassImpl EMPTY_BOARD = new BoardClassImpl();

  private final Map<PositionTransformer, BitBoard> map;

  private BoardClassImpl() {
    map = new EnumMap(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      map.put(transformer, BitBoard.emptyBoard(transformer.getBoardRowNumber()));
    }
  }

  private BoardClassImpl(BoardClassImpl boardClass, int i, int j, StoneType stoneType) {
    map = new EnumMap(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      int ti = transformer.getI(i, j);
      int tj = transformer.getJ(i, j);
      map.put(transformer, boardClass.map.get(transformer).set(ti, tj, stoneType));
    }
  }

  private BoardClassImpl(GameBoard gameBoard) {
    map = new EnumMap(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      map.put(transformer, BitBoard.fromGameBoard(gameBoard, transformer));
    }
  }

  @Override
  public BoardClassImpl withPositionSet(int i, int j, StoneType stoneType) {
    return new BoardClassImpl(this, i, j, stoneType);
  }

  @Override
  public boolean matchesAny(StoneType stoneType, PatternType patternType) {
    return Iterables.any(PATTERNS.get(stoneType, patternType), (p) -> p.matches(this));
  }

  @Override
  public Iterable<Pattern> getMatchingPatterns(StoneType stoneType, PatternType patternType) {
    return Iterables.filter(PATTERNS.get(stoneType, patternType), (p) -> p.matches(this));
  }

  @Override
  public BitBoard getBoard(PositionTransformer transformer) {
    return map.get(transformer);
  }

  static class Factory implements BoardClass.Factory {

    @Override
    public BoardClass fromGameBoard(GameBoard gameBoard) {
      if (gameBoard instanceof BoardClassImpl) {
        return (BoardClassImpl) gameBoard;
      }
      return new BoardClassImpl(gameBoard);
    }

    @Override
    public BoardClass getEmptyBoard() {
      return EMPTY_BOARD;
    }
  }
}
