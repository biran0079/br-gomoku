package common.boardclass;

import com.google.common.collect.Iterables;
import common.*;
import model.GameBoard;

import java.util.EnumMap;

import static common.PositionTransformer.*;

/**
 * Class of bit boards by PositionTransformer operation.
 */
class BoardClassImpl implements BoardClass {

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

  private static final BoardClassImpl EMPTY_BOARD = new BoardClassImpl();

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
    return Iterables.any(Patterns.get(stoneType, patternType), (p) -> matches(p));
  }

  @Override
  public Iterable<Pattern> getMatchingPatterns(StoneType stoneType, PatternType patternType) {
    return Iterables.filter(Patterns.get(stoneType, patternType), (p) -> matches(p));
  }

  @Override
  public BitBoard getBoard(PositionTransformer transformer) {
    return map.get(transformer);
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
    return matchesAny(stoneType, PatternType.FIVE);
  }

  private boolean matches(Pattern pattern) {
    int row = getBoard(pattern.getTransformer()).getRow(pattern.getRowIndex());
    return (row & pattern.getMask()) == pattern.getPattern();
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
