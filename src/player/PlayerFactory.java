package player;

import common.StoneType;
import view.ClickCallbackManager;

import javax.inject.Inject;

/**
 * Factory for players.
 */
public class PlayerFactory {

  private final ClickCallbackManager clickCallbackManager;

  @Inject
  PlayerFactory(ClickCallbackManager clickCallbackManager) {
    this.clickCallbackManager = clickCallbackManager;
  }

  public Player createHumanPlayer(String name, StoneType stoneType) {
    return new Human(name, stoneType, clickCallbackManager);
  }

  public Player createAlphaBetaSearchPlayer(String name, StoneType stoneType) {
    return new MinMaxSearchPlayer(name, stoneType);
  }
}
