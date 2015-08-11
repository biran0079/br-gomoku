package player;

import common.StoneType;
import model.GameBoard;
import model.Position;

public interface Player {

	Position makeMove(GameBoard gameBoard) throws InterruptedException;

	StoneType getStoneType();

  boolean isHuman();
}
