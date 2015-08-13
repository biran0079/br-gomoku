package ai.minmax;

import static org.junit.Assert.assertEquals;

import com.google.common.base.MoreObjects;

import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardClassUtil;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ai.AI;
import model.Position;

public class MinMaxSearchTest {

  private final BoardClass emptyBoard = BoardClassUtil.DEFAULT_FACTORY.getEmptyBoard();

  private final AI d3 = MinMaxSearch.newBuilder()
      .withName("d3")
      .withMaxDepth(3)
      .build();

  private final AI d4 = MinMaxSearch.newBuilder()
      .withName("d4")
      .withMaxDepth(4)
      .build();

  @Test
  public void d3WithEmptyBoard() {
    String expectedMoves = "ArrayList{[[7, 7], [8, 8], [6, 6], [6, 7], [7, 5], [8, 9], [7, 6], [7, 8], [9, 10], [6, 8], [5, 8], [8, 10], [8, 11], [9, 8], [10, 8], [5, 6], [4, 5], [5, 7], [7, 9], [8, 6], [8, 7], [7, 10], [6, 11], [4, 4], [5, 5], [3, 5], [9, 11], [10, 11], [9, 12], [9, 9], [9, 13], [9, 14], [5, 11], [7, 11], [6, 5], [8, 5], [5, 4], [4, 3], [6, 3], [3, 6], [7, 3], [7, 4], [10, 9], [7, 12], [10, 7], [10, 10], [9, 7], [11, 7], [10, 6], [10, 5], [11, 8], [12, 7], [12, 9], [13, 10], [7, 2], [8, 1], [6, 2], [6, 4], [5, 1], [8, 4], [9, 3], [8, 3], [8, 2], [5, 2], [9, 2], [10, 2], [9, 4], [9, 1], [9, 5], [9, 6], [10, 4], [7, 1], [6, 1], [10, 1], [11, 1], [2, 4], [4, 6], [2, 6], [1, 7], [2, 5], [2, 3], [2, 7], [2, 8], [11, 3], [12, 4], [11, 11], [12, 12], [3, 4], [1, 6], [3, 3], [3, 2], [3, 7]]}";
    assertEquals(expectedMoves,
        play(emptyBoard, new AI[]{d3, d3}, new StoneType[]{StoneType.BLACK, StoneType.WHITE}));
  }

  @Test
  public void d4WithEmptyBoard() {
    String expectedMoves =
        "ArrayList{[[7, 7], [8, 8], [6, 6], [8, 9], [8, 6], [6, 7], [6, 8], [5, 9], [9, 5], [10, 4], [9, 6], [5, 6], [7, 8], [5, 8], [5, 7], [6, 9], [7, 9], [7, 10], [8, 10], [4, 6], [9, 11]]}";
    assertEquals(expectedMoves,
        play(emptyBoard, new AI[]{d4, d4}, new StoneType[]{StoneType.BLACK, StoneType.WHITE}));
  }

  private String play(BoardClass boardClass, AI[] ai, StoneType[] stoneType) {
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