package controller;

import model.Position;
import player.Player;

import javax.inject.Inject;

/**
 * Represents session of a single game.
 */
class GameSession {

  private final GameController gameController;
  private boolean sessionSopped = false;
  private volatile boolean waitingForHumanMove = false;

  @Inject
  GameSession(GameController gameController) {
    this.gameController = gameController;
  }

  public void newGameStart(Player[] players) {
    int idx = 0;
    while (!sessionSopped && !Thread.currentThread().isInterrupted()) {
      makeMove(players[idx]);
      idx = 1 - idx;
    }
  }

  public boolean isWaitingForHumanMove() {
    return waitingForHumanMove;
  }

  private void makeMove(Player player) {
    try {
      if (player.isHuman()) {
        waitingForHumanMove = true;
      }
      Position position = player.makeMove(gameController.getGameBoard());
      if (player.isHuman()) {
        waitingForHumanMove = false;
      }
      gameController.putPieceOn(position, player.getStoneType());
    } catch (InterruptedException e) {
      sessionSopped = true;
      return;
    }
    if (gameController.getGameBoard().wins(player.getStoneType())) {
      gameController.gameOver(player);
      sessionSopped = true;
    } else if (gameController.getGameBoard().isFull()) {
      gameController.gameOver(null);
      sessionSopped = true;
    }
  }
}
