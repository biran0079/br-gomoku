package view;

import common.Constants;
import common.StoneType;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

	private final int panel_height = Constants.BOARD_SIZE * UIConstants.SQUARE_HEIGHT;
	private final int panel_width = Constants.BOARD_SIZE * UIConstants.SQUARE_WIDTH;
	private final GameSquare[][] square;

  @Inject
	public GamePanel(GameSquareFactory gameSquareFactory) {
		this.setPreferredSize(new Dimension(panel_width, panel_height));
		this.setLayout(new GridLayout(Constants.BOARD_SIZE, Constants.BOARD_SIZE));
		square = new GameSquare[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
		for(int i = 0; i < Constants.BOARD_SIZE; i++)
			for(int j = 0; j < Constants.BOARD_SIZE; j++){
				square[i][j] = gameSquareFactory.createGameSquare(i, j);
				this.add(square[i][j]);
			}
	}

	public void putPieceOn(int i,int j, StoneType type){
		square[i][j].putPiece(type);
	}

	public void removePieceOn(int i,int j){
		square[i][j].removePiece();
	}

	public void clearBoard() {
		for(int i=0;i< Constants.BOARD_SIZE;i++)
			for(int j=0;j< Constants.BOARD_SIZE;j++){
				square[i][j].removePiece();
			}
	}
}
