package controller;

import common.StoneType;
import model.GameBoard;
import model.History;
import model.HistoryEntry;
import model.Position;
import player.Player;
import player.PlayerFactory;
import view.UI;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class GameControllerImpl implements GameController {

	private final History history;
	private final UI ui;
  private final Provider<GameSessoin> gameSessoinProvider;
  private final ExecutorService gameSessionExecutor = Executors.newSingleThreadExecutor();
  private final PlayerFactory playerFactory;
  private final GameBoard.Factory gameBoardFactory;

  private GameBoard gameBoard;

  private volatile Future<?> currentGameSessionFuture;

  @Inject
	GameControllerImpl(History history,
                     GameBoard.Factory gameBoardFactory,
                     UI ui,
                     Provider<GameSessoin> gameSessoinProvider,
                     PlayerFactory playerFactory) {
		this.history = history;
		this.gameBoardFactory = gameBoardFactory;
    this.ui = ui;
    this.gameSessoinProvider = gameSessoinProvider;
    this.playerFactory = playerFactory;
		ui.addUndoActionListener((e) -> undo());
		ui.addNewGameActionListener((e) -> restartGame());
	}

  @Override
  public void startGame() {
    currentGameSessionFuture = gameSessionExecutor.submit(() -> {
      try {
        gameSessoinProvider.get().newGameStart(initializeGame());
      } catch (Throwable e) {
        e.printStackTrace();
        throw e;
      }
    });
  }

  @Override
  public void undo() {
    if (history.size() < 2
        || !gameSessoinProvider.get().isWaitingForHumanMove()) {
      return;
    }
    HistoryEntry historyEntry;
    historyEntry = history.popLastEntry();
    ui.removePieceOn(historyEntry.getLastMove());
    historyEntry = history.popLastEntry();
    ui.removePieceOn(historyEntry.getLastMove());
    gameBoard = historyEntry.getGameBoard();
	}

  @Override
	public void putPieceOn(final Position position, final StoneType piece) {
    history.recordMove(gameBoard, position, piece);
    gameBoard = gameBoard.withPositionSet(
        position.getRowIndex(), position.getColumnIndex(), piece);
		ui.putPieceOn(position, piece);
	}

  @Override
  public void gameOver(Player winner) {
    if (winner != null) {
      ui.win(winner, () -> startGame());
    } else {
      ui.draw(() -> startGame());
    }
  }

  @Override
  public GameBoard getGameBoard() {
    return gameBoard;
  }

  private Player[] initializeGame() {
    ui.clearBoard();
    history.clear();
    gameBoard = gameBoardFactory.getEmptyBoard();
    PlayerType[] types = ui.getSelectedPlayerTypes();
    return new Player[] {
        createPlayer(types[0], "player1", StoneType.BLACK),
        createPlayer(types[1], "player2", StoneType.WHITE)
    };
  }

  private Player createPlayer(PlayerType type, String name, StoneType stoneType) {
    switch (type) {
      case HUMAN:
        return playerFactory.createHumanPlayer("Human-" + name, stoneType);
      case AI:
        return playerFactory.createAlphaBetaSearchPlayer("AI-" + name, stoneType);
      default:
        throw new RuntimeException();
    }
  }

  private void restartGame() {
    if (currentGameSessionFuture != null) {
      currentGameSessionFuture.cancel(true);
    }
    startGame();
  }
}
