package model;

import common.StoneType;

/**
 * Abstraction of game board.
 */
public interface GameBoard extends ReadOnlyGameBoard {

  void set(Position position, StoneType stoneType);

  StoneType[][] toArray();

  void initialize();
}
