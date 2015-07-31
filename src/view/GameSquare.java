package view;

import common.Square;
import model.Position;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameSquare extends JButton implements ActionListener {

	private final Position position;
	private final ClickCallback clickClickCallback;

	public GameSquare(int i, int j, ClickCallback clickClickCallback) {
		this.position = Position.create(i, j);
		this.setIcon(UIConstants.NOTHING_ICON);
		this.addActionListener(this);
		this.setBorderPainted(false);
		this.setEnabled(false);
		this.clickClickCallback = clickClickCallback;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		clickClickCallback.click(position);
	}

	public void removePiece(){
		this.setDisabledIcon(UIConstants.NOTHING_ICON);
		this.setEnabled(true);
	}

	public void putPiece(Square square){
		switch (square) {
			case BLACK_PIECE:
				this.setDisabledIcon(UIConstants.BLACK_ICON);
				break;
			case WHITE_PIECE:
				this.setDisabledIcon(UIConstants.WHITE_ICON);
				break;
			default:
				throw new RuntimeException();
		}
		this.setEnabled(false);
	}
}
