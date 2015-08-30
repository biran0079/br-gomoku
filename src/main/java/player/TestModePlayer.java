package player;

import common.StoneType;
import view.ClickCallbackManager;

import javax.inject.Inject;
import java.util.function.Supplier;

/**
 * Player for test mode.
 */
class TestModePlayer extends HumanPlayer {

  private final Supplier<StoneType> stoneTypeSupplier;

  @Inject
  TestModePlayer(Supplier<StoneType> stoneTypeSupplier,
                 ClickCallbackManager clickCallbackManager) {
    super("Test player", StoneType.NOTHING, clickCallbackManager);
    this.stoneTypeSupplier = stoneTypeSupplier;
  }

  @Override
  public StoneType getStoneType() {
    return stoneTypeSupplier.get();
  }
}
