package ai.minmax;

import ai.AI;
import ai.candidatemoveselector.CandidateMovesSelectors;
import ai.evaluator.SimplePatternEvaluator;
import com.google.common.base.MoreObjects;
import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardFactories;
import model.Position;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MinMaxSearchTest {

  private final BoardClass<?> emptyBoard =
      BoardFactories.FOR_PATTERN.getEmptyBoard();

  private final AI d3 = MinMaxSearch.defaultBuilderForPattern()
      .withName("d3")
      .withEvaluator(new SimplePatternEvaluator())
      .withCandidateMoveSelector(CandidateMovesSelectors.FOR_TEST)
      .withMaxDepth(3)
      .build();

  private final AI d4 = MinMaxSearch.defaultBuilderForPattern()
      .withName("d4")
      .withEvaluator(new SimplePatternEvaluator())
      .withCandidateMoveSelector(CandidateMovesSelectors.FOR_TEST)
      .withMaxDepth(4)
      .build();

  @Test
  public void d3_withEmptyBoard() {
    String expectedMoves = "ArrayList{[[7, 7], [8, 8], [6, 6], [6, 7], [7, 5], [8, 9], [7, 4], [7, 3], [7, 6], [7, 8], [5, 6], [4, 6], [4, 7], [3, 8], [4, 8], [3, 9], [5, 7], [8, 4], [6, 5], [8, 3], [5, 4], [4, 3], [5, 5], [5, 3], [5, 8]]}";
    assertEquals(expectedMoves,
        play(emptyBoard, new AI[]{d3, d3}, new StoneType[]{StoneType.BLACK, StoneType.WHITE}));
  }

  @Test
  public void d4_withEmptyBoard() {
    String expectedMoves = "ArrayList{[[7, 7], [8, 8], [6, 6], [8, 9], [8, 7], [9, 7], [6, 7], [7, 9], [6, 10], [5, 9], [4, 9], [10, 6], [11, 5], [6, 9], [9, 9], [9, 8], [5, 7], [4, 7], [7, 5], [4, 8], [5, 8], [3, 10], [6, 4], [6, 3], [5, 3], [4, 2], [6, 5], [6, 8], [9, 5], [5, 5], [8, 5], [7, 6], [10, 5]]}";
    assertEquals(expectedMoves,
        play(emptyBoard, new AI[]{d4, d4}, new StoneType[]{StoneType.BLACK, StoneType.WHITE}));
  }

  private String play(BoardClass<?> boardClass, AI[] ai, StoneType[] stoneType) {
    int i = 0;
    List<Position> result = new ArrayList<>();
    while (!boardClass.wins(StoneType.BLACK)
        && !boardClass.wins(StoneType.WHITE)
        && !boardClass.isFull()) {
      Position move = ai[i].nextMove(boardClass, stoneType[i]);
      boardClass = boardClass.withPositionSet(
          move.getRowIndex(), move.getColumnIndex(), stoneType[i]);
      result.add(move);
      i = 1 - i;
    }
    return MoreObjects.toStringHelper(result).addValue(result).toString();
  }
}