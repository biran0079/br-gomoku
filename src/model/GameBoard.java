package model;

import common.Square;
import model.Position;

/**
 * Abstraction of game board.
 */
public interface GameBoard {

  Square get(Position position);

  void set(Position position, Square square);

  Square[][] toArray();

  void initialize();
}
