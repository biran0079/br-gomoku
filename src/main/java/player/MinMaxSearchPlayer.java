package player;

import ai.AI;
import ai.minmax.MinMaxSearch;
import ai.minmax.transitiontable.SmartTransitionTable;
import ai.threatbasedsearch.CompeleteThreatSearchAI;
import common.StoneType;
import model.GameBoard;
import model.Position;

public class MinMaxSearchPlayer implements Player {

  private final String name;
  private final StoneType stoneType;
  private final AI minMaxSearch = new CompeleteThreatSearchAI(
      MinMaxSearch.defaultBuilderForThreat()
          .withTransitionTableFactory(SmartTransitionTable::new)
          .withAlgorithm(MinMaxSearch.Algorithm.MINMAX)
          .withMaxDepth(7)
          .useKillerHeuristic()
          .build(),
      5);

  public MinMaxSearchPlayer(String s, StoneType stoneType) {
    this.name = s;
    this.stoneType = stoneType;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public Position makeMove(GameBoard gameBoard) {
    return minMaxSearch.nextMove(gameBoard, stoneType);
  }

  @Override
  public StoneType getStoneType() {
    return stoneType;
  }

  @Override
  public boolean isHuman() {
    return false;
  }
}
