package common;

import com.google.common.collect.ImmutableList;
import model.Position;

/**
 * Defines a BitBoard pattern.
 */
public class Pattern {
  private final int rowIndex;
  private final int pattern;
  private final int mask;
  private final PositionTransformer transformer;
  private final StoneType stoneType;
  private final ImmutableList<Position> defensiveMoves;

  Pattern(int rowIndex,
          int pattern,
          int mask,
          PositionTransformer transformer,
          StoneType stoneType,
          ImmutableList<Position> defensiveMoves) {
    this.rowIndex = rowIndex;
    this.pattern = pattern;
    this.mask = mask;
    this.transformer = transformer;
    this.stoneType = stoneType;
    this.defensiveMoves = defensiveMoves;
  }

  public ImmutableList<Position> getDefensiveMoves() {
    return defensiveMoves;
  }

  public int getRowIndex() {
    return rowIndex;
  }

  public int getPattern() {
    return pattern;
  }

  public int getMask() {
    return mask;
  }

  public PositionTransformer getTransformer() {
    return transformer;
  }

  public StoneType getStoneType() {
    return stoneType;
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("transformer: ")
        .append(transformer)
        .append(", row: ")
        .append(rowIndex)
        .append(", mask: ")
        .append(mask)
        .append(", pattern: ")
        .append(pattern)
        .toString();
  }
}
