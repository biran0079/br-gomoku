package player;

import model.GameBoard;
import model.Position;
import common.Square;

public interface Player {

	Position makeMove(GameBoard gameBoard) throws InterruptedException;

	Square getStoneType();
}
