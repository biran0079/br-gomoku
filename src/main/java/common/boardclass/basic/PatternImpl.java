package common.boardclass.basic;

import com.google.common.collect.ImmutableSet;
import common.PositionTransformer;
import common.StoneType;
import common.pattern.AbstractPattern;
import common.pattern.PatternType;
import model.Position;

/**
 * Basic implementation of a pattern.
 */
class PatternImpl extends AbstractPattern {

  PatternImpl(int rowIndex,
              int pattern,
              int mask,
              PositionTransformer transformer,
              StoneType stoneType,
              ImmutableSet<Position> defensiveMoves,
              PatternType patternType) {
    super(rowIndex, pattern, mask, transformer, stoneType, defensiveMoves, patternType);
  }
}
