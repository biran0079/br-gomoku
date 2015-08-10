package model;

import common.StoneType;

/**
 * Read only game board interface.
 */
public interface ReadOnlyGameBoard {

  StoneType get(int i, int j);

  boolean isFull();

  boolean isEmpty();
}
