package view;

import common.PlayerType;
import common.StoneType;
import model.Position;
import player.Player;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.ActionListener;

public class UI {
  private final GameFrame gameFrame;
  private final GamePanel gamePanel;
  private final ControlPanel controlPanel;

  @Inject
  public UI(GameFrame gameFrame, ControlPanel controlPanel, GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    this.controlPanel = controlPanel;
    this.gameFrame = gameFrame;
  }

  public void clearBoard() {
    SwingUtilities.invokeLater(() -> gamePanel.clearBoard());
  }

  public PlayerType[] getSelectedPlayerTypes() {
    return controlPanel.getPlayerTypes();
  }

  public void removePieceOn(final Position position) {
    SwingUtilities.invokeLater(
        () -> gamePanel.removePieceOn(position.getRowIndex(), position.getColumnIndex()));
  }

  public void putPieceOn(final Position position, final StoneType stoneType) {
    SwingUtilities.invokeLater(() ->
        gamePanel.putPieceOn(position.getRowIndex(), position.getColumnIndex(), stoneType));
  }

  public void addUndoActionListener(ActionListener actionListener) {
    controlPanel.addUndoActionListener(actionListener);
  }

  public void addNewGameActionListener(ActionListener actionListener) {
    controlPanel.addNewGameActionListener(actionListener);
  }

  public void draw(Runnable restart) {
    String title = "Draw!";
    String message = "Do you want to start a new game?";
    SwingUtilities.invokeLater(() -> newDialog(title, message, restart));
  }

  public void win(final Player p, Runnable restart){
    String title = p.toString() + "wins!";
    String message = "Do you want to start a new game?";
    SwingUtilities.invokeLater(() -> newDialog(title, message, restart));
  }

  private void newDialog(String title, String message, Runnable okCallback) {
    int n = JOptionPane.showOptionDialog(
        gameFrame,
        message,
        title,
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
        null, // icon
        null, // options
        null); //initialValue
    if(n == JOptionPane.OK_OPTION){
      okCallback.run();
    }
  }
}
