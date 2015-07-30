package view;

import common.PlayerType;
import model.Position;
import common.Square;
import player.Player;

import javax.swing.*;
import java.awt.event.ActionListener;

public class UI implements ClickCallbackManager {
  private final GameFrame gameFrame;
  private final GamePanel gamePanel;
  private final ControlPanel controlPanel;

  private ClickCallback clickCallback;

  private final ClickCallback delegateCallback = new ClickCallback() {
    @Override
    public void click(Position position) {
      if (clickCallback != null) {
        clickCallback.click(position);
      }
    }
  };

  public UI() {
    this.gamePanel = new GamePanel(delegateCallback);
    this.controlPanel = new ControlPanel();
    this.gameFrame = new GameFrame(gamePanel, controlPanel);
  }

  public void setClickCallback(final ClickCallback callback) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        clickCallback = callback;
      }
    });
  }

  public void win(final Player p, final ActionListener actionListener){
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new WinDialog(gameFrame, p.toString(), actionListener);
      }
    });
  }

  public void clearBoard() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        gamePanel.clearBoard();
      }
    });
  }

  public PlayerType[] getSelectedPlayerTypes() {
    return controlPanel.getPlayerTypes();
  }

  public void removePieceOn(final Position position) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        gamePanel.removePieceOn(position.getRowIndex(), position.getColumnIndex());
      }
    });
  }

  public void putPieceOn(final Position position, final Square stoneType) {
    SwingUtilities.invokeLater(new Runnable () {
      @Override
      public void run() {
        gamePanel.putPieceOn(position.getRowIndex(), position.getColumnIndex(), stoneType);
      }
    });
  }

  public void addUndoActionListener(ActionListener actionListener) {
    controlPanel.addUndoActionListener(actionListener);
  }

  public void addNewGameActionListener(ActionListener actionListener) {
    controlPanel.addNewGameActionListener(actionListener);
  }
}
