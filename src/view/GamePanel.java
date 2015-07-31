package view;

import common.Constants;
import common.Square;
import model.Position;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

	private final int panel_height = Constants.ROW_NUM * UIConstants.SQUARE_HEIGHT;
	private final int panel_width = Constants.COL_NUM * UIConstants.SQUARE_WIDTH;
	private final GameSquare[][] square;

  @Inject
	public GamePanel(GameSquareFactory gameSquareFactory) {
		this.setPreferredSize(new Dimension(panel_width, panel_height));
		this.setLayout(new GridLayout(Constants.ROW_NUM, Constants.COL_NUM));
		square = new GameSquare[Constants.ROW_NUM][Constants.COL_NUM];
		for(int i = 0; i < Constants.ROW_NUM; i++)
			for(int j = 0; j < Constants.COL_NUM; j++){
				square[i][j] = gameSquareFactory.createGameSquare(i, j);
				this.add(square[i][j]);
			}
	}

	public void putPieceOn(int i,int j, Square type){
		square[i][j].putPiece(type);
	}

	public void removePieceOn(int i,int j){
		square[i][j].removePiece();
	}

	public void clearBoard() {
		for(int i=0;i< Constants.ROW_NUM;i++)
			for(int j=0;j< Constants.COL_NUM;j++){
				square[i][j].removePiece();
			}
	}
}
