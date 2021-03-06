package player;

import com.google.inject.assistedinject.Assisted;
import common.StoneType;
import model.GameBoard;
import model.Position;
import view.ClickCallbackManager;

import javax.inject.Inject;

class HumanPlayer implements Player {

	private final String name;
	private final StoneType stoneType;
  private final ClickCallbackManager clickCallbackManager;

	private Position move;

  @Inject
  HumanPlayer(@Assisted String name,
              @Assisted StoneType stoneType,
              ClickCallbackManager clickCallbackManager) {
		this.name = name;
		this.stoneType = stoneType;
    this.clickCallbackManager = clickCallbackManager;
	}

	@Override
	public String toString(){
		return name;
	}
	
	private synchronized void humanMove(int i, int j){
		this.move = Position.of(i, j);
		notify();
	}

  private void beforeMove() {
    clickCallbackManager.setClickCallback(
        (position) -> humanMove(position.getRowIndex(), position.getColumnIndex()));
  }

	private void afterMove() {
    clickCallbackManager.disableClickCallback();
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
	public StoneType getStoneType() {
		return stoneType;
	}

  @Override
  public boolean isHuman() {
    return true;
  }
}
