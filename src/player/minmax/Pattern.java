package player.minmax;

import common.Square;

import java.util.Objects;

/**
 * Defines a BitBoard pattern.
 */
public class Pattern {
  private final int rowIndex;
  private final int pattern;
  private final int mask;
  private final PositionTransformer transformer;
  private final Square stoneType;

  Pattern(int rowIndex,
          int pattern,
          int mask,
          PositionTransformer transformer,
          Square stoneType) {
    this.rowIndex = rowIndex;
    this.pattern = pattern;
    this.mask = mask;
    this.transformer = transformer;
    this.stoneType = stoneType;
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

  public Square getStoneType() {
    return stoneType;
  }

  @Override
  public int hashCode() {
    return rowIndex ^ pattern ^ mask ^ transformer.ordinal();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pattern)) {
      return false;
    }
    Pattern p = (Pattern) o;
    return rowIndex == p.rowIndex
        && pattern == p.pattern
        && mask == p.mask
        && transformer == p.transformer;
  }
}
