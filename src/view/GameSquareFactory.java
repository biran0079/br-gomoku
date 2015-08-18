package view;

import javax.inject.Inject;

/**
 * Factory for GameSquare object.
 */
class GameSquareFactory {

  private final ClickCallback clickClickCallback;

  @Inject
  GameSquareFactory(ClickCallback clickClickCallback) {
    this.clickClickCallback = clickClickCallback;
  }

  GameSquare createGameSquare(int i, int j) {
    return new GameSquare(i, j, clickClickCallback);
  }
}
