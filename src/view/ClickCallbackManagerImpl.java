package view;

import javax.inject.Inject;
import javax.swing.SwingUtilities;

import model.Position;

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
  public void disableClickCallback() {
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
