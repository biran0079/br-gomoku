package player;

import common.StoneType;
import view.ClickCallbackManager;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.function.Supplier;

/**
 * Factory for players.
 */
public interface PlayerFactory {

  @Named("human") Player createHumanPlayer(String name, StoneType stoneType);

  @Named("ai")Player createAIPlayer(String name, StoneType stoneType);

  @Named("test")Player createTestModePlayer();
}
