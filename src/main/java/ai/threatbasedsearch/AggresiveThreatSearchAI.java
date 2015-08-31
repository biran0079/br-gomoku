package ai.threatbasedsearch;

import ai.AI;
import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardFactories;
import common.pattern.Threat;
import model.GameBoard;
import model.Position;

/**
 * Regular AI backed by complete threat search.
 */
public class AggresiveThreatSearchAI implements AI {

  private final AI delgate;
  private final int threatSearchDepth;
  private final AggresiveThreatSearch aggresiveThreatSearch = new AggresiveThreatSearch();

  public AggresiveThreatSearchAI(AI delgate, int threatSearchDepth) {
    this.delgate = delgate;
    this.threatSearchDepth = threatSearchDepth;
  }

  @Override
  public Position nextMove(GameBoard gameBoard, StoneType stoneType) {
    BoardClass<Threat> boardClass = BoardFactories.FOR_THREAT.fromGameBoard(gameBoard);
    Position move = aggresiveThreatSearch.winningMove(boardClass, stoneType, threatSearchDepth);
    if (move != null) {
      return move;
    }
    return delgate.nextMove(boardClass, stoneType);
  }

  @Override
  public String toString() {
    return delgate.toString() + "-threat-based-" + threatSearchDepth;
  }
}
