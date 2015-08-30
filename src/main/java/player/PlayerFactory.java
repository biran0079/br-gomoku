package player;

import common.StoneType;
import view.ClickCallbackManager;

import javax.inject.Inject;
import java.util.function.Supplier;

/**
 * Factory for players.
 */
public class PlayerFactory {

  private final ClickCallbackManager clickCallbackManager;
  private final TestModePlayer testModePlayer;

  @Inject
  PlayerFactory(ClickCallbackManager clickCallbackManager, Supplier<StoneType> stoneTypeSupplier) {
    this.clickCallbackManager = clickCallbackManager;
    this.testModePlayer = new TestModePlayer(stoneTypeSupplier, clickCallbackManager);
  }

  public Player createHumanPlayer(String name, StoneType stoneType) {
    return new Human(name, stoneType, clickCallbackManager);
  }

  public Player createAlphaBetaSearchPlayer(String name, StoneType stoneType) {
    return new MinMaxSearchPlayer(name, stoneType);
  }

  public Player createTestModePlayer() {
    return testModePlayer;
  }
}
