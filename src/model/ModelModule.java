package model;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import common.boardclass.BoardFactories;

/**
 * Module for model objects.
 */
public class ModelModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(HistoryImpl.class).in(Singleton.class);
    bind(History.class).to(HistoryImpl.class);
    bind(GameBoard.Factory.class)
        .toInstance(BoardFactories.BOARD_CLASS_WITH_MATCHING_PATTERNS_FACTORY);
  }
}
