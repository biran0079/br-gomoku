package player;

import java.util.Random;
import game.*;

public class RandomAI implements Player {

	Random rand = new Random();
	private String name;
	public RandomAI(String s){
		this.name=s;
	}
	
	@Override
	public String toString(){
		return name;
	}

	@Override
	public void makeMove() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int r = Constants.ROW_NUM, c = Constants.COL_NUM;
		int i, j;
		while (true) {
			i = rand.nextInt(r);
			j = rand.nextInt(c);
			if (Game.getSquare(i, j) == Square.NOTHING)
				break;
		}
		Game.putPieceOn(i, j);
	}
}
