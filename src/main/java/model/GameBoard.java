package model;

import common.StoneType;

/**
 * Abstraction of game board.
 */
public interface GameBoard {

  StoneType get(int i, int j);

  default StoneType get(Position p) {
    return get(p.getRowIndex(), p.getColumnIndex());
  }

  boolean isFull();

  boolean isEmpty();

  GameBoard withPositionSet(int i, int j, StoneType stoneType);

  boolean wins(StoneType stoneType);

  int getStoneCount();

  interface Factory {

    GameBoard getEmptyBoard();
  }
}
