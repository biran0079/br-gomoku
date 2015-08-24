package controller;

import player.Player;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Controls game sessions.
 */
class SessionController {

  private final ExecutorService gameSessionExecutor = Executors.newSingleThreadExecutor();
  private volatile GameSession currentSession;
  private volatile Future<?> currentGameSessionFuture;

  private final Provider<GameSession> gameSessionProvider;

  @Inject
  SessionController(Provider<GameSession> gameSessionProvider) {
    this.gameSessionProvider = gameSessionProvider;
  }

  void startGame(Player[] players) {
    if (currentGameSessionFuture != null) {
      currentGameSessionFuture.cancel(true);
    }
    currentGameSessionFuture = gameSessionExecutor.submit(() -> {
      try {
        currentSession = gameSessionProvider.get();
        currentSession.newGameStart(players);
      } catch (Throwable e) {
        e.printStackTrace();
        throw e;
      }
    });
  }

  boolean isWaitingForHumanMove() {
    return currentSession != null && currentSession.isWaitingForHumanMove();
  }
}
