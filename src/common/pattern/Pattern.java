package common.pattern;

import com.google.common.collect.ImmutableList;

import common.PositionTransformer;
import common.StoneType;
import common.boardclass.BoardClass;

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

  public PositionTransformer getTransformer() {
    return transformer;
  }

  public StoneType getStoneType() {
    return stoneType;
  }

  public boolean matches(BoardClass boardClass) {
    return (boardClass.getBoard(transformer).getRow(rowIndex) & mask) == pattern;
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