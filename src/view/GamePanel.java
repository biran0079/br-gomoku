package view;
import game.Constants;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

	private int panel_height = Constants.ROW_NUM * UIConstants.SQUARE_HEIGHT,
			panel_width = Constants.COL_NUM * UIConstants.SQUARE_WIDTH;
	private GameSquare[][] square;
	private static GamePanel instance=null;
	private GamePanel() {
		this.setPreferredSize(new Dimension(panel_width, panel_height));
		this.setLayout(new GridLayout(Constants.ROW_NUM, Constants.COL_NUM));
		square=new GameSquare[Constants.ROW_NUM][Constants.COL_NUM];
		for(int i=0;i< Constants.ROW_NUM;i++)
			for(int j=0;j< Constants.COL_NUM;j++){
				square[i][j]=new GameSquare(i,j);
				this.add(square[i][j]);
			}
	}
	public static GamePanel getInstance(){
		if(instance==null){
			instance=new GamePanel();
		}
		return instance;
	}
	public static GameSquare getGameSquare(int i,int j){
		return getInstance().square[i][j];
	}
	public static void putPieceOn(int i,int j){
		getGameSquare(i,j).putPiece();
	}
	public static void removePieceOn(int i,int j){
		getGameSquare(i,j).removePiece();
	}
	public static void clearBoard() {
		GamePanel gp=getInstance();
		for(int i=0;i< Constants.ROW_NUM;i++)
			for(int j=0;j< Constants.COL_NUM;j++){
				gp.square[i][j].setEnabled(true);
			}
	}
}
