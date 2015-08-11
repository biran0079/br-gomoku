package common;

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

import java.util.EnumMap;
import java.util.List;
import java.util.stream.Stream;

import model.ReadOnlyGameBoard;

/**
 * Class of bit boards by PositionTransformer operation.
 */
public class BoardClass implements ReadOnlyGameBoard {

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

  private BoardClass(BoardClass boardClass, int i, int j, StoneType stoneType) {
    map = new EnumMap(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      int ti = transformer.getI(i, j);
      int tj = transformer.getJ(i, j);
      map.put(transformer, boardClass.map.get(transformer).set(ti, tj, stoneType));
    }
  }

  private BoardClass(ReadOnlyGameBoard gameBoard) {
    map = new EnumMap(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      map.put(transformer, BitBoard.fromGameBoard(gameBoard, transformer));
    }
  }

  public BoardClass withPositionSet(int i, int j, StoneType stoneType) {
    return new BoardClass(this, i, j, stoneType);
  }
 public static BoardClass fromGameBoard(ReadOnlyGameBoard gameBoard) {
    return new BoardClass(gameBoard);
  }

  public boolean matchesAny(List<Pattern> patterns) {
    return patterns.stream().anyMatch(this::matches);
  }

  public Stream<Pattern> filterMatchedPatterns(List<Pattern> patterns) {
    return patterns.stream().filter(this::matches);
  }

  public BitBoard getBoard(PositionTransformer transformer) {
    return map.get(transformer);
  }

  private boolean matches(Pattern pattern) {
    int row = getBoard(pattern.getTransformer()).getRow(pattern.getRowIndex());
    return (row & pattern.getMask()) == pattern.getPattern();
  }

  @Override
  public boolean isEmpty() {
    return getBoard(IDENTITY).isEmpty();
  }

  @Override
  public String toString() {
    return getBoard(PositionTransformer.IDENTITY).toString();
  }

  @Override
  public StoneType get(int i, int j) {
    return getBoard(IDENTITY).get(i, j);
  }

  @Override
  public boolean isFull() {
    BitBoard board = getBoard(IDENTITY);
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        if (board.get(i, j) == StoneType.NOTHING)
          return false;
      }
    }
    return true;
  }
}
