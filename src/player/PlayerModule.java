package player;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * Module for Players.
 */
public class PlayerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(PlayerFactory.class).in(Singleton.class);
  }
}
