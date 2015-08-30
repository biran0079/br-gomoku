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
  private final Provider<GameSession> gameSessionProvider;
  private volatile GameSession currentSession;
  private volatile Future<?> currentGameSessionFuture;

  @Inject
  SessionController(Provider<GameSession> gameSessionProvider) {
    this.gameSessionProvider = gameSessionProvider;
  }

  void startGame(Player[] players) {
    if (currentGameSessionFuture != null) {
      // kill earlier session
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

  boolean isHumanVsComputer() {
    return currentSession.isHumanVsComputer();
  }
}
