package game;

/**
 * Abstraction of game board.
 */
public interface GameBoard {

  Square get(Position position);

  void set(Position position, Square square);
}
