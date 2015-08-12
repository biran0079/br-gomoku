package common;

import com.google.common.collect.Iterables;
import model.GameBoard;

import java.util.EnumMap;

import static common.PositionTransformer.*;

/**
 * Class of bit boards by PositionTransformer operation.
 */
public class BoardClass implements GameBoard {

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

  private static final BoardClass EMPTY_BOARD = new BoardClass();

  private BoardClass() {
    map = new EnumMap(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      map.put(transformer, BitBoard.emptyBoard(transformer.getBoardRowNumber()));
    }
  }

  private BoardClass(BoardClass boardClass, int i, int j, StoneType stoneType) {
    map = new EnumMap(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      int ti = transformer.getI(i, j);
      int tj = transformer.getJ(i, j);
      map.put(transformer, boardClass.map.get(transformer).set(ti, tj, stoneType));
    }
  }

  private BoardClass(GameBoard gameBoard) {
    map = new EnumMap(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      map.put(transformer, BitBoard.fromGameBoard(gameBoard, transformer));
    }
  }

  @Override
  public BoardClass withPositionSet(int i, int j, StoneType stoneType) {
    return new BoardClass(this, i, j, stoneType);
  }

  public static BoardClass fromGameBoard(GameBoard gameBoard) {
    if (gameBoard instanceof  BoardClass) {
      return (BoardClass) gameBoard;
    }
    return new BoardClass(gameBoard);
  }

  public boolean matchesAny(Iterable<Pattern> patterns) {
    return Iterables.any(patterns, (p) -> matches(p));
  }

  public Iterable<Pattern> filterMatchedPatterns(Iterable<Pattern> patterns) {
    return Iterables.filter(patterns, (p) -> matches(p));
  }

  public BitBoard getBoard(PositionTransformer transformer) {
    return map.get(transformer);
  }

  private boolean matches(Pattern pattern) {
    int row = getBoard(pattern.getTransformer()).getRow(pattern.getRowIndex());
    return (row & pattern.getMask()) == pattern.getPattern();
  }

  @Override
  public int getStoneCount() {
    return getBoard(IDENTITY).getStoneCount();
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
  public StoneType get(int i, int j) {
    return getBoard(IDENTITY).get(i, j);
  }

  @Override
  public boolean isFull() {
    return getStoneCount() == Constants.BOARD_SIZE * Constants.BOARD_SIZE;
  }

  @Override
  public boolean wins(StoneType stoneType) {
    return matchesAny(Patterns.getGoalPatterns(stoneType));
  }

  public static BoardClass getEmptyBoard() {
    return EMPTY_BOARD;
  }
}
