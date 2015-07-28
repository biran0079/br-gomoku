package view;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import game.Game;
import player.Human;
import view.UIConstants;

public class GameSquare extends JButton implements ActionListener {
	private int row_idx, col_idx;

	public GameSquare(int i, int j) {
		row_idx = i;
		col_idx = j;
		this.setIcon(UIConstants.NOTHING_ICON);
		this.addActionListener(this);
		this.setBorderPainted(false);
		this.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (Game.getCurrentPlayer() instanceof Human) {
			((Human)Game.getCurrentPlayer()).humanMove(row_idx,col_idx);
		}
	}
	public void removePiece(){
		this.setDisabledIcon(UIConstants.NOTHING_ICON);
		this.setEnabled(true);
	}
	public void putPiece(){
		switch (Game.getWhosTurn()) {
		case BLACK:
			this.setDisabledIcon(UIConstants.BLACK_ICON);
			this.setEnabled(false);
			break;
		case WHITE:
			this.setDisabledIcon(UIConstants.WHITE_ICON);
			this.setEnabled(false);
			break;
		}
	}
}
