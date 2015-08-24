package ai.minmax.transitiontable;


import common.boardclass.BoardClass;

public interface TransitionSet {

  boolean contains(BoardClass<?> boardClass);

  void add(BoardClass<?> boardClass);
}
