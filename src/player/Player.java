package player;

import game.GameBoard;
import game.Position;
import game.Square;

public interface Player {

	Position makeMove(GameBoard gameBoard) throws InterruptedException;

	Square getStoneType();
}
