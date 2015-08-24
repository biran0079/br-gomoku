package common.boardclass;

import ai.candidatemoveselector.CandidateMoveSelectorUtil;
import ai.minmax.transitiontable.TransitionSet;
import ai.minmax.transitiontable.TransitionSetImpl;
import com.google.common.collect.Lists;
import common.Constants;
import common.StoneType;
import model.Position;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Generator of BoardClass objects.
 */
public class BoardClassGenerator {

  public Iterable<BoardClass<?>> generateBoardWithStones(int n) {
    if (n == 0) {
      return Collections.singletonList(
          BoardFactories.FOR_PATTERN.getEmptyBoard());
    }
    if (n == 1) {
      return Collections.singletonList(
          BoardFactories.FOR_PATTERN.getEmptyBoard()
              .withPositionSet(Constants.BOARD_SIZE / 2,
                  Constants.BOARD_SIZE / 2, StoneType.BLACK));
    }
    return () -> new UniqueBoardIterator(new BoardGeneratingIterator(n));
  }

  private class UniqueBoardIterator implements Iterator<BoardClass<?>> {

    private final TransitionSet transitionSet = new TransitionSetImpl();
    private final Iterator<BoardClass<?>> boardIterator;
    private BoardClass<?> nextBoardClass;

    UniqueBoardIterator(Iterator<BoardClass<?>> boardIterator) {
      this.boardIterator = boardIterator;
    }

    @Override
    public boolean hasNext() {
      while (nextBoardClass == null && boardIterator.hasNext()) {
        BoardClass<?> t = boardIterator.next();
        if (!transitionSet.contains(t)) {
          nextBoardClass = t;
        }
      }
      return nextBoardClass != null;
    }

    @Override
    public BoardClass<?> next() {
      try {
        transitionSet.add(nextBoardClass);
        return nextBoardClass;
      } finally {
        nextBoardClass = null;
      }
    }
  }

  private class BoardGeneratingIterator implements Iterator<BoardClass<?>> {

    private final Iterator<BoardClass<?>> boardIterator;
    private Iterator<Position> moveIterator = Collections.emptyIterator();
    private BoardClass<?> baseBoard;

    BoardGeneratingIterator(int n) {
      this.boardIterator = generateBoardWithStones(n - 1).iterator();
    }

    @Override
    public boolean hasNext() {
      while (!moveIterator.hasNext() && boardIterator.hasNext()) {
        baseBoard = boardIterator.next();
        moveIterator = shuffle(CandidateMoveSelectorUtil.getNeighboringMoves(baseBoard)).iterator();
      }
      return moveIterator.hasNext();
    }

    @Override
    public BoardClass<?> next() {
      Position move = moveIterator.next();
      return baseBoard.withPositionSet(
          move.getRowIndex(),
          move.getColumnIndex(),
          baseBoard.getStoneCount() % 2 == 0 ? StoneType.BLACK : StoneType.WHITE);
    }

    private Iterable<Position> shuffle(Iterable<Position> moves) {
      List<Position> movesCopy = Lists.newArrayList(moves);
      Collections.shuffle(movesCopy);
      return movesCopy;
    }
  }
}
