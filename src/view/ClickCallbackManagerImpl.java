package view;

import model.Position;

import javax.inject.Inject;
import javax.swing.*;

/**
 * Manager of game board click event call back.
 */
public class ClickCallbackManagerImpl implements ClickCallbackManager {

  private ClickCallback clickCallback;

  @Inject
  ClickCallbackManagerImpl() {

  }

  @Override
  public void setClickCallback(ClickCallback callback) {
    SwingUtilities.invokeLater(() -> clickCallback = callback);
  }

  @Override
  public void disableClickCallbacl() {
    setClickCallback(null);
  }

  @Override
  public void click(Position position) {
    SwingUtilities.invokeLater(() -> {
      if (clickCallback != null) {
        clickCallback.click(position);
      }
    });
  }
}
