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
import javax.swing.*;

class GameControllerImpl implements GameController {

	private final History history;
	private final UI ui;
  private final PlayerFactory playerFactory;
  private final GameBoard.Factory gameBoardFactory;
  private final SessionController sessionController;

  private GameBoard gameBoard;

  @Inject
	GameControllerImpl(History history,
                     GameBoard.Factory gameBoardFactory,
                     UI ui,
                     SessionController sessionController,
                     PlayerFactory playerFactory) {
		this.history = history;
		this.gameBoardFactory = gameBoardFactory;
    this.ui = ui;
    this.sessionController = sessionController;
    this.playerFactory = playerFactory;
		ui.addUndoActionListener((e) -> undo());
    ui.addNewGameActionListener((e) -> sessionController.startGame(initializeGame()));
    ui.addPrintListener((e) -> System.out.println(gameBoard));
    ui.addTestModeListener((e) -> {
      JToggleButton tBtn = (JToggleButton)e.getSource();
      if (tBtn.isSelected()) {
        sessionController.startGame(new Player[] {playerFactory.createTestModePlayer()});
      }
    });
	}

  @Override
  public void startGame() {
    sessionController.startGame(initializeGame());
  }

  @Override
  public void undo() {
    if (history.size() < 1
        || !sessionController.isWaitingForHumanMove()) {
      return;
    }
    HistoryEntry historyEntry;
    historyEntry = history.popLastEntry();
    ui.removePieceOn(historyEntry.getLastMove());
    if (sessionController.isHumanVsComputer()) {
      historyEntry = history.popLastEntry();
      ui.removePieceOn(historyEntry.getLastMove());
      gameBoard = historyEntry.getGameBoard();
    }
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
      ui.win(winner, this::startGame);
    } else {
      ui.draw(this::startGame);
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
        return playerFactory.createAIPlayer("AI-" + name, stoneType);
      default:
        throw new RuntimeException();
    }
  }
}
