package player.minmax;

import com.google.common.collect.ImmutableList;
import common.Square;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for Patterns class.
 */
public class PatternsTest {

  private static final Square E = Square.NOTHING;
  private static final Square W = Square.WHITE_PIECE;
  private static final Square B = Square.BLACK_PIECE;

  @Test
  public void testGoalNumber() {
    assertEquals(572, Patterns.BLACK_GOALS.size());
    assertEquals(572, Patterns.WHITE_GOALS.size());
  }

  @Test
  public void testHorizontalGoalMatch() {
    testSinglePattern(
        new Square[][]{
            {B, B, B, B, B},
        },
        PositionTransformer.IDENTITY,
        Square.BLACK_PIECE,
        Patterns.BLACK_GOALS);

    testSinglePattern(
        new Square[][]{
            {B, B, W, B, B},
            {E, E, W, W, W, W, W},
        },
        PositionTransformer.IDENTITY,
        Square.WHITE_PIECE,
        Patterns.WHITE_GOALS);
  }

  @Test
  public void testVerticalGoalMatch() {
    testSinglePattern(
        new Square[][]{
            {B},
            {B},
            {B},
            {B},
            {B},
        },
        PositionTransformer.CLOCK_90,
        Square.BLACK_PIECE,
        Patterns.BLACK_GOALS);

    testSinglePattern(
        new Square[][]{
            {},
            {},
            {E, W},
            {E, W},
            {E, W},
            {E, W},
            {E, W},
        },
        PositionTransformer.CLOCK_90,
        Square.WHITE_PIECE,
        Patterns.WHITE_GOALS);
  }

  @Test
  public void testDiagonalGoalMatch() {
    testSinglePattern(
        new Square[][]{
            {B},
            {E, B},
            {E, E, B},
            {E, E, E, B},
            {E, E, E, E, B},
        },
        PositionTransformer.LEFT_DIAGONAL,
        Square.BLACK_PIECE,
        Patterns.BLACK_GOALS);

    testSinglePattern(
        new Square[][]{
            {E, E, E, E, W},
            {E, E, E, W},
            {E, E, W},
            {E, W},
            {W},
        },
        PositionTransformer.RIGHT_DIAGONAL,
        Square.WHITE_PIECE,
        Patterns.WHITE_GOALS);
  }

  @Test
  public void testHorizontalStraitFourMatch() {
    testSinglePattern(
        new Square[][]{
            {B, B, B, B, E},
        },
        PositionTransformer.IDENTITY,
        Square.BLACK_PIECE,
        Patterns.BLACK_STRAIT_FOUR);

    testSinglePattern(
        new Square[][]{
            {B, B, W, B, B},
            {E, E, E, W, W, W, W, B},
        },
        PositionTransformer.IDENTITY,
        Square.WHITE_PIECE,
        Patterns.WHITE_STRAIT_FOUR);
  }

  @Test
  public void testVerticalStraitFourMatch() {
    testSinglePattern(
        new Square[][]{
            {B},
            {B},
            {B},
            {B},
        },
        PositionTransformer.CLOCK_90,
        Square.BLACK_PIECE,
        Patterns.BLACK_STRAIT_FOUR);

    testSinglePattern(
        new Square[][]{
            {E, W},
            {E, W},
            {E, W},
            {E, W},
        },
        PositionTransformer.CLOCK_90,
        Square.WHITE_PIECE,
        Patterns.WHITE_STRAIT_FOUR);
  }

  @Test
  public void testDiagonalStraitFourMatch() {
    testSinglePattern(
        new Square[][]{
            {B},
            {E, B},
            {E, E, B},
            {E, E, E, B},
        },
        PositionTransformer.LEFT_DIAGONAL,
        Square.BLACK_PIECE,
        Patterns.BLACK_STRAIT_FOUR);

    testSinglePattern(
        new Square[][]{
            {E, E, E, E, W},
            {E, E, E, W},
            {E, E, W},
            {E, W},
            {E},
        },
        PositionTransformer.RIGHT_DIAGONAL,
        Square.WHITE_PIECE,
        Patterns.WHITE_STRAIT_FOUR);
  }

  @Test
  public void testHorizontalThreeMatch() {
    testSinglePattern(
        new Square[][]{
            {E, B, B, B, E, E},
        },
        PositionTransformer.IDENTITY,
        Square.BLACK_PIECE,
        Patterns.BLACK_THREE);

    testSinglePattern(
        new Square[][]{
            {B, B, W, B, B},
            {E, E, E, W, W, W, E, B},
        },
        PositionTransformer.IDENTITY,
        Square.WHITE_PIECE,
        Patterns.WHITE_THREE);
  }

  @Test
  public void testVerticalThreeMatch() {
    testSinglePattern(
        new Square[][]{
            {E},
            {B},
            {B},
            {B},
        },
        PositionTransformer.CLOCK_90,
        Square.BLACK_PIECE,
        Patterns.BLACK_THREE);

    testSinglePattern(
        new Square[][]{
            {},
            {E, W},
            {E, W},
            {E, W},
        },
        PositionTransformer.CLOCK_90,
        Square.WHITE_PIECE,
        Patterns.WHITE_THREE);
  }

  @Test
  public void testDiagonalThreeMatch() {
    testSinglePattern(
        new Square[][]{
            {E},
            {E, B},
            {E, E, B},
            {E, E, E, B},
        },
        PositionTransformer.LEFT_DIAGONAL,
        Square.BLACK_PIECE,
        Patterns.BLACK_THREE);

    testSinglePattern(
        new Square[][]{
            {},
            {},
            {E, E, E, W},
            {E, E, W},
            {E, W},
            {E},
        },
        PositionTransformer.RIGHT_DIAGONAL,
        Square.WHITE_PIECE,
        Patterns.WHITE_THREE);
  }

  private void testSinglePattern(Square[][] board,
                                 PositionTransformer orientation,
                                 Square stoneType,
                                 ImmutableList<Pattern> candidates) {
    BoardClass boardClass = createBoard(board);
    List<Pattern> patterns = getMatcching(boardClass, candidates);
    assertEquals(1, patterns.size());
    assertEquals(orientation, patterns.get(0).getTransformer());
    assertEquals(stoneType, patterns.get(0).getStoneType());
  }

  private List<Pattern> getMatcching(BoardClass boardClass, ImmutableList<Pattern> candidates) {
    List<Pattern> result = new ArrayList<>();
    for (Pattern p : candidates) {
      if (boardClass.matches(p)) {
        result.add(p);
      }
    }
    return result;
  }

  private BoardClass createBoard(Square[][] board) {
    BoardClass boardClass = BoardClass.emptyBoardClass();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] != Square.NOTHING) {
          boardClass = boardClass.set(i, j, board[i][j]);
        }
      }
    }
    return boardClass;
  }
}