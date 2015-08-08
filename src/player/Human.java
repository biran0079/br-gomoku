package player;

import common.Square;
import model.GameBoard;
import model.Position;
import view.ClickCallback;
import view.ClickCallbackManager;

import javax.inject.Inject;

class Human implements Player {

	private final String name;
	private final Square stoneType;
  private final ClickCallbackManager clickCallbackManager;

	private Position move;

  Human(String name, Square stoneType, ClickCallbackManager clickCallbackManager) {
		this.name = name;
		this.stoneType = stoneType;
    this.clickCallbackManager = clickCallbackManager;
	}

	@Override
	public String toString(){
		return name;
	}
	
	private synchronized void humanMove(int i, int j){
		this.move = Position.create(i, j);
		notify();
	}

  private void beforeMove() {
    clickCallbackManager.setClickCallback(
        (position) -> humanMove(position.getRowIndex(), position.getColumnIndex()));
  }

	private void afterMove() {
    clickCallbackManager.disableClickCallbacl();
  }

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
