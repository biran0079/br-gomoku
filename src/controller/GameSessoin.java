package controller;

import common.Utils;
import model.Position;
import player.Player;

import javax.inject.Inject;

/**
 * Represents session of a single game.
 */
class GameSessoin {

  private final GameController gameController;
  private boolean sessionSopped = false;

  @Inject
  GameSessoin(GameController gameController) {
    this.gameController = gameController;
  }

  public void newGameStart(Player[] players) {
    int idx = 0;
    while (!sessionSopped && !Thread.currentThread().isInterrupted()) {
      makeMove(players[idx]);
      idx = 1 - idx;
    }
  }

  private void makeMove(Player player) {
    try {
      Position position = player.makeMove(gameController.getGameBoard());
      gameController.putPieceOn(position, player.getStoneType());
    } catch (InterruptedException e) {
      sessionSopped = true;
      return;
    }
    if (Utils.playerWins(gameController.getGameBoard(), player.getStoneType())) {
      gameController.gameOver(player);
      sessionSopped = true;
    } else if (gameController.getGameBoard().isFull()) {
      gameController.gameOver(null);
      sessionSopped = true;
    }
  }
}
