package player;

import common.Square;
import player.minmax.AlphaBetaSearch;
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

  public Player createHumanPlayer(String name, Square stoneType) {
    return new Human(name, stoneType, clickCallbackManager);
  }

  public Player createAlphaBetaSearchPlayer(String name, Square stoneType) {
    return new AlphaBetaSearch(name, stoneType);
  }
}
