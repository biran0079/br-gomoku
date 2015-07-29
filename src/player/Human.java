package player;

import game.GameBoard;
import game.Position;
import game.Square;

public class Human implements Player {

	private final String name;
	private final Square stoneType;

	private Position move;

	public Human(String name, Square stoneType) {
		this.name = name;
		this.stoneType = stoneType;
	}

	@Override
	public String toString(){
		return name;
	}
	
	public synchronized void humanMove(int i, int j){
		this.move = Position.create(i, j);
		notify();
	}

	protected void beforeMove() {}

	protected void afterMove() {}

	@Override
	public synchronized Position makeMove(GameBoard gameBoard) throws InterruptedException {
		try {
			beforeMove();
			wait();
			return move;
		} finally {
			afterMove();
		}
	}

	@Override
	public Square getStoneType() {
		return stoneType;
	}
}
