package view;

import common.StoneType;
import controller.PlayerType;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

class ControlPanel extends JPanel {

	private final JButton newGame, undo, print;
  private final JToggleButton testMode;
  private final ButtonGroup testStoneType;
  private final JRadioButton black, white;
  private final JComboBox<PlayerType> player1, player2;

  @Inject
	public ControlPanel() {
		newGame = new JButton("new game");
		undo = new JButton("undo");
    testMode = new JToggleButton("test mode");
    print = new JButton("print");
		PlayerType[] types = { PlayerType.HUMAN, PlayerType.AI };
		player1 = new JComboBox<>(types);
		player2 = new JComboBox<>(types);
    testStoneType = new ButtonGroup();
    black = new JRadioButton("black");
    white = new JRadioButton("white");
    testStoneType.add(black);
    testStoneType.add(white);
		this.setLayout(new GridLayout(0, 1));
		this.add(newGame);
    this.add(undo);
    this.add(player1);
    this.add(player2);
    this.add(print);
    this.add(testMode);
    this.add(black);
    this.add(white);
    black.setVisible(false);
    white.setVisible(false);
    testMode.addActionListener((e) -> {
      JToggleButton tBtn = (JToggleButton)e.getSource();
      boolean visible = tBtn.isSelected();
      black.setVisible(visible);
      white.setVisible(visible);
    });
	}

	public PlayerType[] getPlayerTypes() {
		return new PlayerType[]{
				(PlayerType) player1.getSelectedItem(),
				(PlayerType) player2.getSelectedItem()
		};
	}

  StoneType getSelectedStoneType() {
    if (black.isSelected()) {
      return StoneType.BLACK;
    }
    return StoneType.WHITE;
  }

	void addUndoActionListener(ActionListener actionListener) {
		undo.addActionListener(actionListener);
	}

	void addNewGameActionListener(ActionListener actionListener) {
		newGame.addActionListener(actionListener);
	}

  void addPrintActionListener(ActionListener actionListener) {
    print.addActionListener(actionListener);
  }

  void addTestModeListener(ActionListener actionListener) {testMode.addActionListener(actionListener);}
}
