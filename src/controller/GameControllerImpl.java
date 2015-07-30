package controller;

import common.PlayerType;
import common.Square;
import model.GameBoard;
import model.GameBoards;
import model.Position;
import player.Player;
import player.Players;
import view.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class GameControllerImpl implements GameController {

	private GameBoard gameBoard;
	private Player player1 = null, player2 = null;
	private final Stack<Position> history;
	private final UI ui;
  private final ExecutorService gameSessionExecutor = Executors.newSingleThreadExecutor();
  private volatile Future<?> currentGameSessionFuture;

	GameControllerImpl() {
		this.history = new Stack<Position>();
		this.gameBoard = GameBoards.createGameBoard();
    this.ui = new UI();
		ui.addUndoActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		ui.addNewGameActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restartGame();
			}
		});
	}

  @Override
  public void startGame() {
    currentGameSessionFuture = gameSessionExecutor.submit(new Runnable() {
      @Override
      public void run() {
        initializeGame();
        new GameSessoin(GameControllerImpl.this)
            .newGameStart(new Player[]{player1, player2});
      }
    });
  }

  @Override
  public void undo() {
		for (int k = 2; k > 0 && !history.isEmpty(); k--) {
			Position position = history.pop();
      ui.removePieceOn(position);
      gameBoard.set(position, Square.NOTHING);
		}
	}

  @Override
	public void putPieceOn(final Position position, final Square piece) {
    history.add(position);
		gameBoard.set(position, piece);
		ui.putPieceOn(position, piece);
	}

  @Override
  public void gameOver(Player winner) {
    ui.win(winner, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        startGame();
      }
    });
  }

  @Override
  public GameBoard getGameBoard() {
    return gameBoard;
  }

  private void initializeGame() {
    ui.clearBoard();
    history.clear();
    gameBoard.initialize();
    PlayerType[] types = ui.getSelectedPlayerTypes();
    player1 = createPlayer(types[0], "player1", Square.BLACK_PIECE);
    player2 = createPlayer(types[1], "player2", Square.WHITE_PIECE);
  }

  private Player createPlayer(PlayerType type, String name, Square stoneType) {
    switch (type) {
      case HUMAN:
        return Players.createHumanPlayer("Human-" + name, stoneType, ui);
      case AI:
        return Players.createAlphaBetaSearchPlayer("AI-" + name, stoneType);
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
