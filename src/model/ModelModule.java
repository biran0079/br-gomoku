package model;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import common.BoardClass;

/**
 * Module for model objects.
 */
public class ModelModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(HistoryImpl.class).in(Singleton.class);
    bind(History.class).to(HistoryImpl.class);

    bind(BoardClass.Factory.class)
        .toInstance(() -> BoardClass.getEmptyBoard());
  }
}
