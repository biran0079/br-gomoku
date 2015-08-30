package player;

import common.StoneType;
import view.ClickCallbackManager;

import java.util.function.Supplier;

/**
 * Player for test mode.
 */
class TestModePlayer extends Human {

  private final Supplier<StoneType> stoneTypeSupplier;

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
