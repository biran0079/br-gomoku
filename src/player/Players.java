package player;

import common.Square;
import view.ClickCallbackManager;

/**
 * Factory for players.
 */
public class Players {

  public static Player createHumanPlayer(
      String name, Square stoneType, ClickCallbackManager clickCallbackManager) {
    return new Human(name, stoneType, clickCallbackManager);
  }

  public static Player createAlphaBetaSearchPlayer(String name, Square stoneType) {
    return new AlphaBetaSearch(name, stoneType);
  }
}
