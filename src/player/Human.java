package player;

import game.Game;

public class Human implements Player{

	private boolean moved;
	private int i,j;
	private String name;
	public Human(String s){
		this.name=s;
	}
	@Override
	public String toString(){
		return name;
	}
	
	public void humanMove(int i,int j){
		this.i=i;
		this.j=j;
		moved=true;
	}

	@Override
	public void makeMove() {
		moved=false;
		while(true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized(this){
				if(moved)break;
				if(Game.getInstance().gameRestarted())return;
			}
		}
		Game.putPieceOn(i,j);
	}


}
