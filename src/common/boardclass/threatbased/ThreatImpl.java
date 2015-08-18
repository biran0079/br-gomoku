package common.boardclass.threatbased;

import com.google.common.collect.ImmutableSet;

import common.PositionTransformer;
import common.StoneType;
import common.pattern.AbstractPattern;
import common.pattern.Threat;

import model.Position;

/**
 * Implementation of threat backed by PatternImpl.
 */
class ThreatImpl extends AbstractPattern implements Threat {

  private final Position offensiveMove;

  ThreatImpl(int rowIndex,
             int pattern,
             int mask,
             PositionTransformer transformer,
             StoneType stoneType,
             ImmutableSet<Position> defensiveMoves,
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
