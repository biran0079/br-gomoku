package common.pattern;

import com.google.common.collect.ImmutableList;
import common.PatternType;
import common.PositionTransformer;
import common.StoneType;
import model.Position;

/**
 * Implementation of threat backed by PatternImpl.
 */
class ThreatImpl extends PatternImpl implements Threat {

  private final Position offensiveMove;

  ThreatImpl(int rowIndex,
              int pattern,
              int mask,
              PositionTransformer transformer,
              StoneType stoneType,
              ImmutableList<Position> defensiveMoves,
              Position offensiveMove) {
    super(rowIndex, pattern, mask, transformer,
        stoneType, defensiveMoves);
    this.offensiveMove = offensiveMove;
  }

  @Override
  public Position getOffensiveMove() {
    return offensiveMove;
  }
}
