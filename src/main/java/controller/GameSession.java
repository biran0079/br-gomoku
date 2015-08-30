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
  private boolean humanVsComputer;
  private volatile boolean waitingForHumanMove = false;

  @Inject
  GameSession(GameController gameController) {
    this.gameController = gameController;
  }

  public void newGameStart(Player[] players) {
    humanVsComputer = players.length == 2
        && players[0].isHuman() != players[1].isHuman();
    int idx = 0;
    while (!sessionSopped && !Thread.currentThread().isInterrupted()) {
      makeMove(players[idx]);
      idx = (idx + 1) % players.length;
    }
  }

  public boolean isWaitingForHumanMove() {
    return waitingForHumanMove;
  }

  public boolean isHumanVsComputer() {
    return humanVsComputer;
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
