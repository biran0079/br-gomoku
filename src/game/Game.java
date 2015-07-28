package game;

import java.util.Stack;

import player.*;
import view.ControlPanel;
import view.GameFrame;
import view.GamePanel;

public class Game {
	private Turn whosTurn;
	private Square[][] chessBoard;
	private static Game instance = null;
	private Player player1 = null, player2 = null;
	private boolean restart;
	private boolean readyToStart;
	private Stack<Position> history;

	private Game() {
		readyToStart = true;
		history = new Stack<Position>();
		chessBoard = new Square[Constants.ROW_NUM][Constants.COL_NUM];
		GameFrame.getInstance();
	}

	public static boolean validPosition(int i, int j) {
		return i >= 0 && i < Constants.ROW_NUM && j >= 0 && j < Constants.COL_NUM;
	}
	public void setReadyToStart(boolean a){
		readyToStart=a;
	}
	private void initializeGame() {
		whosTurn = Turn.BLACK;
		restart = false;
		history.clear();
		for (int i = 0; i < Constants.ROW_NUM; i++)
			for (int j = 0; j < Constants.COL_NUM; j++) {
				chessBoard[i][j] = Square.NOTHING;
			}
		GamePanel.clearBoard();
		player1= ControlPanel.getInstance().getPlayer1Instance();
		player2=ControlPanel.getInstance().getPlayer2Instance();
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

	public boolean gameRestarted(){
		return restart;
	}

	public void humanRestard() {
		this.restart = true;
	}

	public void newGameStart() {
		initializeGame();
		while (true) {
			player1.makeMove();
			if (restart)
				break;
			if (playerWins(chessBoard,Square.BLACK_PIECE)) {
				GameFrame.win(player1);
				break;
			}
			player2.makeMove();
			if (playerWins(chessBoard,Square.WHITE_PIECE)) {
				GameFrame.win(player2);
				break;
			}
		}
	}

	public static Square[][] getBoard() {
		return getInstance().chessBoard;
	}

	public static Turn getWhosTurn() {
		return getInstance().whosTurn;
	}

	public static void setWhosTurn(Turn t) {
		getInstance().whosTurn = t;
	}

	public static Game getInstance() {
		if (instance == null) {
			instance = new Game();
		}
		return instance;
	}

	private static void switchTurn() {
		if (Game.getWhosTurn() == Turn.BLACK) {
			Game.setWhosTurn(Turn.WHITE);
		} else {
			Game.setWhosTurn(Turn.BLACK);
		}
	}

	public static Player getCurrentPlayer() {
		if (getWhosTurn() == Turn.BLACK) {
			return getInstance().player1;
		} else {
			return getInstance().player2;
		}
	}

	public static Stack<Position> getHistory() {
		return Game.getInstance().history;
	}

	public static void undo() {
		int i, j, k, humanPlayer = 0;
		if (Game.getInstance().player1 instanceof Human)
			humanPlayer++;
		if (Game.getInstance().player2 instanceof Human)
			humanPlayer++;
		if (humanPlayer == 0)
			k = 0;
		else if (humanPlayer == 1)
			k = 2;
		else
			k = 1;
		for (; k > 0 && !getHistory().isEmpty(); k--) {
			i = Game.getHistory().peek().getRowIndex();
			j = Game.getHistory().peek().getColumnIndex();
			Game.getHistory().pop();
			Game.removePieceOn(i, j);
		}
	}

	public static void removePieceOn(int i, int j) {
		getInstance().chessBoard[i][j] = Square.NOTHING;
		GamePanel.removePieceOn(i, j);
	}

	public static void putPieceOn(int i, int j) {
		Game.getHistory().add(Position.create(i, j));
		switch (Game.getWhosTurn()) {
		case BLACK:
			getInstance().chessBoard[i][j] = Square.BLACK_PIECE;
			break;
		case WHITE:
			getInstance().chessBoard[i][j] = Square.WHITE_PIECE;
			break;
		}
		GamePanel.putPieceOn(i, j);
		switchTurn();
	}

	public static Square getSquare(int i, int j) {
		return getInstance().chessBoard[i][j];
	}
	public void play() {
		while(true){
			while(!readyToStart){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			newGameStart();
			setReadyToStart(false);
			if(restart)readyToStart=true;	//Press "New Game" Button
		}
	}
}
