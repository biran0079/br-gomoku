package player.minmax;

import static player.minmax.PositionTransformer.*;

import com.google.common.collect.Iterables;
import common.Square;
import model.GameBoard;

import java.util.EnumMap;

/**
 * Class of bit boards by PositionTransformer operation.
 */
public class BoardClass {

  private final EnumMap<PositionTransformer, BitBoard> map;

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

  private static final BoardClass EMPTY = new BoardClass();

  private BoardClass() {
    map = new EnumMap(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      map.put(transformer, new BitBoard(transformer.getBoardRowNumber()));
    }
  }

  private BoardClass(GameBoard gameBoard) {
    map = new EnumMap(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      map.put(transformer, BitBoard.fromGameBoard(gameBoard, transformer));
    }
  }

  BoardClass set(int i, int j, Square stoneType) {
    BoardClass result = new BoardClass();
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      int ti = transformer.getI(i, j);
      int tj = transformer.getJ(i, j);
      result.map.put(transformer, map.get(transformer).set(ti, tj, stoneType));
    }
    return result;
  }

  BitBoard getBoard(PositionTransformer transformer) {
    return map.get(transformer);
  }

  boolean matches(Pattern pattern) {
    int row = getBoard(pattern.getTransformer()).getRow(pattern.getRowIndex());
    return (row & pattern.getMask()) == pattern.getPattern();
  }

  @Override
  public String toString() {
    return getBoard(PositionTransformer.IDENTITY).toString();
  }

  static BoardClass emptyBoardClass() {
    return EMPTY;
  }
  static BoardClass fromGameBoard(GameBoard gameBoard) {
    return new BoardClass(gameBoard);
  }

  boolean matchesAny(Iterable<Pattern> patterns) {
    return Iterables.any(patterns, (p) -> matches(p));
  }

  Iterable<Pattern> filterMatchedPatterns(Iterable<Pattern> patterns) {
    return Iterables.filter(patterns, (p) -> matches(p));
  }

  boolean isEmpty() {
    return getBoard(IDENTITY).isEmpty();
  }
}
