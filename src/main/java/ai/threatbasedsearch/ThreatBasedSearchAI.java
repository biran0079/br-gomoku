package ai.threatbasedsearch;

import ai.AI;
import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardFactories;
import common.pattern.Threat;
import model.GameBoard;
import model.Position;

/**
 * AI based on threat based search.
 */
public class ThreatBasedSearchAI implements AI {

  private final AI delgate;
  private final ThreatBasedSearch threatBasedSearch = new ThreatBasedSearch();

  public ThreatBasedSearchAI(AI delgate) {
    this.delgate = delgate;
  }

  @Override
  public Position nextMove(GameBoard gameBoard, StoneType stoneType) {
    BoardClass<Threat> boardClass = BoardFactories.FOR_THREAT.fromGameBoard(gameBoard);
    Position move = threatBasedSearch.winningMove(boardClass, stoneType);
    if (move != null) {
      return move;
    }
    return delgate.nextMove(boardClass, stoneType);
  }

  @Override
  public String toString() {
    return delgate.toString() + "-threat-based-search";
  }
}
