package view;

import controller.PlayerType;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {

	private final JButton newGame, undo;
	private final JComboBox<PlayerType> player1, player2;

  @Inject
	public ControlPanel() {
		newGame = new JButton("new game");
		undo = new JButton("undo");
		PlayerType[] types = { PlayerType.HUMAN, PlayerType.AI };
		player1 = new JComboBox(types);
		player2 = new JComboBox(types);
		this.setLayout(new FlowLayout());
		this.add(newGame);
		this.add(undo);
		this.add(player1);
		this.add(player2);
	}

	public PlayerType[] getPlayerTypes() {
		return new PlayerType[]{
				(PlayerType) player1.getSelectedItem(),
				(PlayerType) player2.getSelectedItem()
		};
	}

	void addUndoActionListener(ActionListener actionListener) {
		undo.addActionListener(actionListener);
	}

	void addNewGameActionListener(ActionListener actionListener) {
		newGame.addActionListener(actionListener);
	}
}
