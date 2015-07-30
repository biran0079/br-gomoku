package game;

import controller.GameControllers;

/**
 * Main class of the game.
 */
class Main {

  public static void main(String[] args) {
    GameControllers.createGameController().startGame();
  }

  private Main() {

  }
}
