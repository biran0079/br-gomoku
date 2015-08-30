package player;

import ai.AI;
import com.google.inject.assistedinject.Assisted;
import common.StoneType;
import model.GameBoard;
import model.Position;

import javax.inject.Inject;

public class AIPlayer implements Player {

  private final String name;
  private final StoneType stoneType;
  private final AI ai;

  @Inject
  public AIPlayer(
      @Assisted String name,
      @Assisted StoneType stoneType,
      AI ai) {
    this.name = name;
    this.stoneType = stoneType;
    this.ai = ai;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public Position makeMove(GameBoard gameBoard) {
    return ai.nextMove(gameBoard, stoneType);
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
