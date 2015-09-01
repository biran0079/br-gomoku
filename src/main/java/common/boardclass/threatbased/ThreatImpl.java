package common.boardclass.threatbased;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import common.Constants;
import common.PositionTransformer;
import common.StoneType;
import common.boardclass.testing.BoardClassUtil;
import common.pattern.AbstractPattern;
import common.pattern.Pattern;
import common.pattern.PatternType;
import common.pattern.Threat;
import model.Position;

/**
 * Implementation of threat backed by PatternImpl.
 */
class ThreatImpl extends AbstractPattern implements Threat {

  private final Position offensiveMove;
  private final ImmutableSet<Position> dependingMoves;

  ThreatImpl(int rowIndex,
             int pattern,
             int mask,
             PositionTransformer transformer,
             StoneType stoneType,
             ImmutableSet<Position> defensiveMoves,
             Position offensiveMove,
             ImmutableSet<Position> dependingMoves,
             PatternType patternType) {
    super(rowIndex, pattern, mask, transformer,
        stoneType, defensiveMoves, patternType);
    this.offensiveMove = offensiveMove;
    this.dependingMoves = dependingMoves;
  }

  @Override
  public Position getOffensiveMove() {
    return offensiveMove;
  }

  @Override
  public boolean dependingOn(Threat threat) {
    return threat.getStoneType() == getStoneType() && dependingMoves.contains(threat.getOffensiveMove());
  }

  @Override
  public boolean covers(Threat threat) {
    switch (getPatternType()) {
      case FIVE:
        return threat.getPatternType() == PatternType.FOUR
            && threat.getDependingMoves().contains(dependingMoves);
      case STRAIT_FOUR:
        return threat.getPatternType() == PatternType.FOUR
            && threat.getDependingMoves().equals(dependingMoves);
      case THREE:
        return getDefensiveMoves().size() == 2
            && threat.getPatternType() == PatternType.THREE
            && threat.getOffensiveMove().equals(offensiveMove)
            && threat.getDefensiveMoves().size() == 3
            && threat.getDependingMoves().equals(dependingMoves);
      default:
        return false;
    }
  }

  @Override
  public ImmutableSet<Position> getDependingMoves() {
    return dependingMoves;
  }

  @Override
  public String toString() {
    char[][] chars = new char[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
    for (int i = 0; i < Constants.BOARD_SIZE; i++)
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        chars[i][j] = BoardClassUtil.EMPTY_CHAR;
      }
    for (Position dep : dependingMoves) {
      chars[dep.getRowIndex()][dep.getColumnIndex()] = BoardClassUtil.BLACK_CHAR;
    }
    if (offensiveMove != null) {
      chars[offensiveMove.getRowIndex()][offensiveMove.getColumnIndex()] = '\uFF38';
    }
    for (Position dep : getDefensiveMoves()) {
      chars[dep.getRowIndex()][dep.getColumnIndex()] = '\uFF24';
    }
    StringBuilder sb = new StringBuilder();
    for (char[] s : chars) {
      sb.append(s);
      sb.append('\n');
    }
    return sb.toString();
  }
}
