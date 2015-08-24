package common.boardclass.basic;

import com.google.common.collect.Iterables;
import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.PatternType;
import org.junit.Test;

import static common.boardclass.testing.BoardClassUtil.*;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for Patterns class.
 */
public class PatternsTest {

  @Test
  public void testGoalNumber() {
    Patterns patterns = new Patterns();
    assertEquals(572, patterns.get(StoneType.BLACK, PatternType.FIVE).size());
    assertEquals(572, patterns.get(StoneType.WHITE, PatternType.FIVE).size());
  }

  @Test
  public void testHorizontalGoalMatch() {
    testSinglePattern(
        new StoneType[][]{
            {B, B, B, B, B},
        },
        StoneType.BLACK,
        PatternType.FIVE);

    testSinglePattern(
        new StoneType[][]{
            {B, B, W, B, B},
            {E, E, W, W, W, W, W},
        },
        StoneType.WHITE,
        PatternType.FIVE);
  }

  @Test
  public void testVerticalGoalMatch() {
    testSinglePattern(
        new StoneType[][]{
            {B},
            {B},
            {B},
            {B},
            {B},
        },
        StoneType.BLACK,
        PatternType.FIVE);

    testSinglePattern(
        new StoneType[][]{
            {},
            {},
            {E, W},
            {E, W},
            {E, W},
            {E, W},
            {E, W},
        },
        StoneType.WHITE,
        PatternType.FIVE);
  }

  @Test
  public void testDiagonalGoalMatch() {
    testSinglePattern(
        new StoneType[][]{
            {B},
            {E, B},
            {E, E, B},
            {E, E, E, B},
            {E, E, E, E, B},
        },
        StoneType.BLACK,
        PatternType.FIVE);

    testSinglePattern(
        new StoneType[][]{
            {E, E, E, E, W},
            {E, E, E, W},
            {E, E, W},
            {E, W},
            {W},
        },
        StoneType.WHITE,
        PatternType.FIVE);
  }

  @Test
  public void testHorizontalStraitFourMatch() {
    testSinglePattern(
        new StoneType[][]{
            {B, B, B, B, E},
        },
        StoneType.BLACK,
        PatternType.FOUR);

    testSinglePattern(
        new StoneType[][]{
            {B, B, W, B, B},
            {E, E, E, W, W, W, W, B},
        },
        StoneType.WHITE,
        PatternType.FOUR);
  }

  @Test
  public void testVerticalStraitFourMatch() {
    testSinglePattern(
        new StoneType[][]{
            {B},
            {B},
            {B},
            {B},
        },
        StoneType.BLACK,
        PatternType.FOUR);

    testSinglePattern(
        new StoneType[][]{
            {E, W},
            {E, W},
            {E, W},
            {E, W},
        },
        StoneType.WHITE,
        PatternType.FOUR);
  }

  @Test
  public void testDiagonalStraitFourMatch() {
    testSinglePattern(
        new StoneType[][]{
            {B},
            {E, B},
            {E, E, B},
            {E, E, E, B},
        },
        StoneType.BLACK,
        PatternType.FOUR);

    testSinglePattern(
        new StoneType[][]{
            {E, E, E, E, W},
            {E, E, E, W},
            {E, E, W},
            {E, W},
            {E},
        },
        StoneType.WHITE,
        PatternType.FOUR);
  }

  @Test
  public void testHorizontalThreeMatch() {
    testSinglePattern(
        new StoneType[][]{
            {E, B, B, B, E, E},
        },
        StoneType.BLACK,
        PatternType.THREE);

    testSinglePattern(
        new StoneType[][]{
            {B, B, W, B, B},
            {E, E, E, W, W, W, E, B},
        },
        StoneType.WHITE,
        PatternType.THREE);
  }

  @Test
  public void testVerticalThreeMatch() {
    testSinglePattern(
        new StoneType[][]{
            {E},
            {B},
            {B},
            {B},
        },
        StoneType.BLACK,
        PatternType.THREE);

    testSinglePattern(
        new StoneType[][]{
            {},
            {E, W},
            {E, W},
            {E, W},
        },
        StoneType.WHITE,
        PatternType.THREE);
  }

  @Test
  public void testDiagonalThreeMatch() {
    testSinglePattern(
        new StoneType[][]{
            {E},
            {E, B},
            {E, E, B},
            {E, E, E, B},
        },
        StoneType.BLACK,
        PatternType.THREE);

    testSinglePattern(
        new StoneType[][]{
            {},
            {},
            {E, E, E, W},
            {E, E, W},
            {E, W},
            {E},
        },
        StoneType.WHITE,
        PatternType.THREE);
  }

  private void testSinglePattern(StoneType[][] board,
                                 StoneType stoneType,
                                 PatternType patternType) {
    BoardClass<?> boardClass = createBoard(board);
    PatternImpl pattern = (PatternImpl)
        Iterables.getOnlyElement(boardClass.getMatchingPatterns(stoneType, patternType));
    assertEquals(stoneType, pattern.getStoneType());
  }
}