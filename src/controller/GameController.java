package controller;

import common.StoneType;

import model.GameBoard;
import model.Position;
import player.Player;

/**
 * Controller of the game state.
 */
public interface GameController {

  void putPieceOn(final Position position, final StoneType piece);

  void undo();

  void gameOver(Player winner);

  GameBoard getGameBoard();

  void startGame();
}
