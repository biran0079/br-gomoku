package player.minmax;

import common.Square;

import java.util.EnumMap;

/**
 * Class of bit boards by PositionTransformer operation.
 */
public class BoardClass {

  private static final BoardClass EMPTY = new BoardClass();

  private final EnumMap<PositionTransformer, BitBoard> map;

  private BoardClass() {
    map = new EnumMap(PositionTransformer.class);
    for (PositionTransformer transformer : PositionTransformer.values()) {
      map.put(transformer, new BitBoard(transformer.getBoardRowNumber()));
    }
  }

  static BoardClass emptyBoardClass() {
    return EMPTY;
  }

  BoardClass set(int i, int j, Square stoneType) {
    BoardClass result = new BoardClass();
    for (PositionTransformer transformer : PositionTransformer.values()) {
      int ti = transformer.getI(i, j);
      int tj = transformer.getJ(i, j);
      result.map.put(transformer, map.get(transformer).set(ti, tj, stoneType));
    }
    return result;
  }

  BitBoard getBoard(PositionTransformer transformer) {
    return map.get(transformer);
  }

  boolean match(Pattern pattern) {
    int row = getBoard(pattern.getTransformer()).getRow(pattern.getRowIndex());
    return (row & pattern.getMask()) == pattern.getPattern();
  }

  @Override
  public String toString() {
    return getBoard(PositionTransformer.IDENTITY).toString();
  }
}
