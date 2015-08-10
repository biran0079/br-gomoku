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

  public void win(final Player p, ActionListener actionListener){
    SwingUtilities.invokeLater(() -> new WinDialog(gameFrame, p.toString(), actionListener));
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

  public void draw(ActionListener actionListener) {
    SwingUtilities.invokeLater(() -> new WinDialog(gameFrame, "Nobody", actionListener));
  }
}
