package ai;

import common.StoneType;

import model.Position;
import model.ReadOnlyGameBoard;

/**
 * Created by biran on 8/9/2015.
 */
public interface AI {

  Position nextMove(ReadOnlyGameBoard gameBoard, StoneType stoneType);
}
