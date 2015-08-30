package common.boardclass.threatbased;

import com.google.common.collect.ImmutableSet;
import common.Constants;
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
  private final ImmutableSet<Position> dependingMoves;

  ThreatImpl(int rowIndex,
             int pattern,
             int mask,
             PositionTransformer transformer,
             StoneType stoneType,
             ImmutableSet<Position> defensiveMoves,
             Position offensiveMove,
             ImmutableSet<Position> dependingMoves) {
    super(rowIndex, pattern, mask, transformer,
        stoneType, defensiveMoves);
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
  public String toString() {
    char[][] chars = new char[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
    for (int i = 0; i < Constants.BOARD_SIZE; i++)
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        chars[i][j] = '_';
      }
    for (Position dep : dependingMoves) {
      chars[dep.getRowIndex()][dep.getColumnIndex()] = 'X';
    }
    chars[offensiveMove.getRowIndex()][offensiveMove.getColumnIndex()] = 'O';
    for (Position dep : getDefensiveMoves()) {
      chars[dep.getRowIndex()][dep.getColumnIndex()] = 'D';
    }
    StringBuilder sb = new StringBuilder();
    for (char[] s : chars) {
      sb.append(s);
      sb.append('\n');
    }
    return sb.toString();
  }
}
