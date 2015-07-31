package player;

import common.Square;
import model.GameBoard;
import model.Position;

public interface Player {

	Position makeMove(GameBoard gameBoard) throws InterruptedException;

	Square getStoneType();
}
