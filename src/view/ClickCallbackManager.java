package view;

/**
 * Manager for the board wide clickable callback.
 */
public interface ClickCallbackManager extends ClickCallback {

  void setClickCallback(final ClickCallback callback);

  void disableClickCallback();
}
