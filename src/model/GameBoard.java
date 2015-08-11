package model;

import common.Constants;
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

  default int getStoneCount() {
    int ct = 0;
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        if (get(i, j) != StoneType.NOTHING) {
          ct++;
        }
      }
    }
    return ct;
  }

  interface Factory {

    GameBoard newEmptyBoard();
  }
}
