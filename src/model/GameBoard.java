package model;

import common.StoneType;

/**
 * Abstraction of game board.
 */
public interface GameBoard {

  StoneType get(int i, int j);

  boolean isFull();

  boolean isEmpty();

  GameBoard withPositionSet(int i, int j, StoneType stoneType);

  boolean wins(StoneType stoneType);

  interface Factory {

    GameBoard newEmptyBoard();
  }
}
