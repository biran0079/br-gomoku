package controller;

import common.PlayerType;
import common.Square;
import model.GameBoard;
import model.History;
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

	private final GameBoard gameBoard;
	private final History history;
	private final UI ui;
  private final Provider<GameSessoin> gameSessoinProvider;
  private final ExecutorService gameSessionExecutor = Executors.newSingleThreadExecutor();
  private final PlayerFactory playerFactory;

  private volatile Future<?> currentGameSessionFuture;

  @Inject
	GameControllerImpl(History history,
                     GameBoard gameBoard,
                     UI ui,
                     Provider<GameSessoin> gameSessoinProvider,
                     PlayerFactory playerFactory) {
		this.history = history;
		this.gameBoard = gameBoard;
    this.ui = ui;
    this.gameSessoinProvider = gameSessoinProvider;
    this.playerFactory = playerFactory;
		ui.addUndoActionListener((e) -> undo());
		ui.addNewGameActionListener((e) -> restartGame());
	}

  @Override
  public void startGame() {
    currentGameSessionFuture = gameSessionExecutor.submit(() -> {
      gameSessoinProvider.get().newGameStart(initializeGame());
    });
  }

  @Override
  public void undo() {
		for (int k = 2; k > 0 && history.hasMore(); k--) {
			Position position = history.getLastMove().getPosition();
      ui.removePieceOn(position);
      gameBoard.set(position, Square.NOTHING);
		}
	}

  @Override
	public void putPieceOn(final Position position, final Square piece) {
    history.recordMove(position, piece);
		gameBoard.set(position, piece);
		ui.putPieceOn(position, piece);
	}

  @Override
  public void gameOver(Player winner) {
    ui.win(winner, (e) -> startGame());
  }

  @Override
  public GameBoard getGameBoard() {
    return gameBoard;
  }

  private Player[] initializeGame() {
    ui.clearBoard();
    history.clear();
    gameBoard.initialize();
    PlayerType[] types = ui.getSelectedPlayerTypes();
    return new Player[] {
        createPlayer(types[0], "player1", Square.BLACK_PIECE),
        createPlayer(types[1], "player2", Square.WHITE_PIECE)
    };
  }

  private Player createPlayer(PlayerType type, String name, Square stoneType) {
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
