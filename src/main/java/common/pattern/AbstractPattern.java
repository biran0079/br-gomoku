package common.pattern;

import com.google.common.collect.ImmutableSet;
import common.Constants;
import common.PositionTransformer;
import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.testing.BoardClassUtil;
import model.Position;

/**
 * Basic implementation of pattern.
 */
public abstract class AbstractPattern implements Pattern {

  private final int rowIndex;
  private final int pattern;
  private final int mask;
  private final PositionTransformer transformer;
  private final StoneType stoneType;
  private final ImmutableSet<Position> defensiveMoves;
  private final PatternType patternType;

  protected AbstractPattern(int rowIndex,
              int pattern,
              int mask,
              PositionTransformer transformer,
              StoneType stoneType,
              ImmutableSet<Position> defensiveMoves,
              PatternType patternType) {
    this.rowIndex = rowIndex;
    this.pattern = pattern;
    this.mask = mask;
    this.transformer = transformer;
    this.stoneType = stoneType;
    this.defensiveMoves = defensiveMoves;
    this.patternType = patternType;
  }

  @Override
  public PatternType getPatternType() {
    return patternType;
  }

  @Override
  public ImmutableSet<Position> getDefensiveMoves() {
    return defensiveMoves;
  }

  @Override
  public StoneType getStoneType() {
    return stoneType;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        int ti = transformer.getI(i, j);
        int tj = transformer.getJ(i, j);
        if (ti == rowIndex && ((mask >> (2 * tj)) & 3) == 3) {
          sb.append(StoneType.fromBits((pattern >> (tj * 2)) & 3));
        } else {
          sb.append(BoardClassUtil.EMPTY_CHAR);
        }
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  @Override
  public boolean matches(BoardClass<?> boardClass) {
    return (boardClass.getBoard(transformer).getRow(rowIndex) & mask) == pattern;
  }
}
