package controller;

/**
 * Factory for GameController.
 */
public class GameControllers {

  public static GameController createGameController() {
    return new GameControllerImpl();
  }
}
