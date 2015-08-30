package view;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import common.StoneType;

import java.util.function.Supplier;

/**
 * Module for view.
 */
public class ViewModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(UI.class).in(Singleton.class);
    bind(GameFrame.class).in(Singleton.class);
    bind(GamePanel.class).in(Singleton.class);
    bind(ControlPanel.class).in(Singleton.class);

    bind(ClickCallbackManagerImpl.class).in(Singleton.class);
    bind(ClickCallback.class).to(ClickCallbackManagerImpl.class);
    bind(ClickCallbackManager.class).to(ClickCallbackManagerImpl.class);

    bind(GameSquareFactory.class).in(Singleton.class);
  }

  @Provides
  @Singleton
  Supplier<StoneType> providesSelectedStoneTypeSupplier(ControlPanel controlPanel) {
    return () -> controlPanel.getSelectedStoneType();
  }
}
