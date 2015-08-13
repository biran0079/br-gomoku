package ai;

import common.StoneType;
import model.GameBoard;
import model.Position;

/**
 * Abstract interface for gomoku AI.
 */
public interface AI {

  Position nextMove(GameBoard gameBoard, StoneType stoneType);
}
