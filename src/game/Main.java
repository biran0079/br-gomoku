package game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import controller.GameController;
import controller.ControllerModule;
import model.ModelModule;
import player.PlayerModule;
import view.ViewModule;

/**
 * Main class of the game.
 */
class Main {

  public static void main(String[] args) {
    Injector injector = Guice.createInjector(
        new ControllerModule(),
        new ModelModule(),
        new ViewModule(),
        new PlayerModule());
    injector.getInstance(GameController.class).startGame();
  }
}
