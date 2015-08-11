package ai;

import common.StoneType;
import model.GameBoard;
import model.Position;

/**
 * Created by biran on 8/9/2015.
 */
public interface AI {

  Position nextMove(GameBoard gameBoard, StoneType stoneType);
}
