package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import java.util.concurrent.*;

import player.*;
import view.*;

public class Game {

	private GameBoard gameBoard;
	private Player player1 = null, player2 = null;
	private final Stack<Position> history;
	private final UI ui;

	public Game() {
		this.history = new Stack<Position>();
		this.gameBoard = new GameBoardImpl();
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

	public static boolean validPosition(int i, int j) {
		return i >= 0 && i < Constants.ROW_NUM && j >= 0 && j < Constants.COL_NUM;
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
				return new Human("Human-" + name, stoneType) {

					@Override
					public void beforeMove() {
						ui.setClickCallback(new ClickCallback() {
							@Override
							public void click(Position position) {
								humanMove(position.getRowIndex(), position.getColumnIndex());
							}
						});
					}

					@Override
					public void afterMove() {
						ui.setClickCallback(null);
					}
				};
			case AI:
				return new AlphaBetaSearch("AI-" + name, stoneType);
			default:
				throw new RuntimeException();
		}
	}

	public static boolean playerWins(Square[][] chessBoard, Square curPiece) {
		int r = Constants.ROW_NUM, c = Constants.COL_NUM;
		int[][] d = { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 } };
		for (int i = 0; i < r; i++)
			for (int j = 0; j < c; j++) {
				for (int k = 0; k < 4; k++) {
					if (validPosition(i + 4 * d[k][0], j + 4 * d[k][1])) {
						int l;
						for (l = 0; l < 5; l++) {
							if (chessBoard[i + l * d[k][0]][j + l * d[k][1]] != curPiece) {
								break;
							}
						}
						if (l == 5)
							return true;
					}
				}
			}
		return false;
	}

	public void undo() {
		for (int k = 2; k > 0 && !history.isEmpty(); k--) {
			removePieceOn(history.pop());
		}
	}

	public void removePieceOn(Position position) {
		ui.removePieceOn(position);
		gameBoard.set(position, Square.NOTHING);
	}

	public void putPieceOn(final Position position, final Square piece) {
		history.add(position);
		gameBoard.set(position, piece);
		ui.putPieceOn(position, piece);
	}

	private void restartGame() {
		if (currentGameSessionFuture != null) {
			currentGameSessionFuture.cancel(true);
		}
		startGame();
	}

	private final ExecutorService gameSessionExecutor = Executors.newSingleThreadExecutor();
	private volatile Future<?> currentGameSessionFuture;

	public void startGame() {
		currentGameSessionFuture = gameSessionExecutor.submit(new Runnable() {
			@Override
			public void run() {
				initializeGame();
				new GameSessoin().newGameStart(new Player[] {player1, player2});
			}
		});
	}

	private class GameSessoin {

		private boolean sessionSopped = false;

		public void newGameStart(Player[] players) {
			int idx = 0;
			while (!sessionSopped && !Thread.currentThread().isInterrupted()) {
				makeMove(players[idx]);
				idx = 1 - idx;
			}
		}

		void makeMove(Player player) {
			try {
				putPieceOn(player.makeMove(gameBoard), player.getStoneType());
			} catch (InterruptedException e) {
				sessionSopped = true;
				return;
			}
			if (playerWins(gameBoard.toArray(), player.getStoneType())) {
				ui.win(player, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						startGame();
					}
				});
				sessionSopped = true;
			}
		}
	}
}
