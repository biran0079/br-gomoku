package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import game.Game;
import player.AlphaBetaSearch;
import player.Human;
import player.Player;

public class ControlPanel extends JPanel {

	private JButton newGame, undo;
	private JComboBox player1, player2;
	private static ControlPanel instance;

	public static ControlPanel getInstance() {
		if (instance == null) {
			instance = new ControlPanel();
		}
		return instance;
	}

	private ControlPanel() {
		newGame = new JButton("new game");
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Game.getInstance().humanRestard();
			}
		});
		undo = new JButton("undo");
		undo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Game.getCurrentPlayer() instanceof Human) {
					Game.undo();
				}
			}
		});
		String[] strs = { "Human", "AI" };
		player1 = new JComboBox(strs);
		player2 = new JComboBox(strs);
		this.setLayout(new FlowLayout());
		this.add(newGame);
		this.add(undo);
		this.add(player1);
		this.add(player2);
	}

	public Player getPlayer1Instance() {
		if (player1.getSelectedItem().equals("Human")) {
			return new Human("Human");
		} else if (player1.getSelectedItem().equals("AI")) {
			return new AlphaBetaSearch("AI");
		}
		try {
			throw new Exception("Ivalid Player: "
					+ player1.getSelectedItem().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Player getPlayer2Instance() {
		if (player2.getSelectedItem().equals("Human")) {
			return new Human("Human");
		} else if (player2.getSelectedItem().equals("AI")) {
			return new AlphaBetaSearch("AI");
		}
		try {
			throw new Exception("Ivalid Player: "
					+ player2.getSelectedItem().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
