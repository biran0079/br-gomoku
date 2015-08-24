package controller;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * Module for Game controller.
 */
public class ControllerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(GameControllerImpl.class).in(Singleton.class);
    bind(GameController.class).to(GameControllerImpl.class);

    bind(SessionController.class).in(Singleton.class);
    bind(GameSession.class);
  }
}
