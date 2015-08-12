package ai.minmax;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import common.*;
import model.Position;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static common.BoardClassUtil.*;

/**
 * Unit tests for Patterns class.
 */
public class PatternsTest {

  @Test
  public void testGoalNumber() {
    assertEquals(572, Patterns.get(StoneType.BLACK, PatternType.FIVE).size());
    assertEquals(572, Patterns.get(StoneType.WHITE, PatternType.FIVE).size());
  }

  @Test
  public void testHorizontalGoalMatch() {
    testSinglePattern(
        new StoneType[][]{
            {B, B, B, B, B},
        },
        PositionTransformer.IDENTITY,
        StoneType.BLACK,
        Patterns.get(StoneType.BLACK, PatternType.FIVE));

    testSinglePattern(
        new StoneType[][]{
            {B, B, W, B, B},
            {E, E, W, W, W, W, W},
        },
        PositionTransformer.IDENTITY,
        StoneType.WHITE,
        Patterns.get(StoneType.WHITE, PatternType.FIVE));
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
        PositionTransformer.CLOCK_90,
        StoneType.BLACK,
        Patterns.get(StoneType.BLACK, PatternType.FIVE));

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
        PositionTransformer.CLOCK_90,
        StoneType.WHITE,
        Patterns.get(StoneType.WHITE, PatternType.FIVE));
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
        PositionTransformer.LEFT_DIAGONAL,
        StoneType.BLACK,
        Patterns.get(StoneType.BLACK, PatternType.FIVE));

    testSinglePattern(
        new StoneType[][]{
            {E, E, E, E, W},
            {E, E, E, W},
            {E, E, W},
            {E, W},
            {W},
        },
        PositionTransformer.RIGHT_DIAGONAL,
        StoneType.WHITE,
        Patterns.get(StoneType.WHITE, PatternType.FIVE));
  }

  @Test
  public void testHorizontalStraitFourMatch() {
    testSinglePattern(
        new StoneType[][]{
            {B, B, B, B, E},
        },
        PositionTransformer.IDENTITY,
        StoneType.BLACK,
        Patterns.get(StoneType.BLACK, PatternType.STRAIT_FOUR));

    testSinglePattern(
        new StoneType[][]{
            {B, B, W, B, B},
            {E, E, E, W, W, W, W, B},
        },
        PositionTransformer.IDENTITY,
        StoneType.WHITE,
        Patterns.get(StoneType.WHITE, PatternType.STRAIT_FOUR));
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
        PositionTransformer.CLOCK_90,
        StoneType.BLACK,
        Patterns.get(StoneType.BLACK, PatternType.STRAIT_FOUR));

    testSinglePattern(
        new StoneType[][]{
            {E, W},
            {E, W},
            {E, W},
            {E, W},
        },
        PositionTransformer.CLOCK_90,
        StoneType.WHITE,
        Patterns.get(StoneType.WHITE, PatternType.STRAIT_FOUR));
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
        PositionTransformer.LEFT_DIAGONAL,
        StoneType.BLACK,
        Patterns.get(StoneType.BLACK, PatternType.STRAIT_FOUR));

    testSinglePattern(
        new StoneType[][]{
            {E, E, E, E, W},
            {E, E, E, W},
            {E, E, W},
            {E, W},
            {E},
        },
        PositionTransformer.RIGHT_DIAGONAL,
        StoneType.WHITE,
        Patterns.get(StoneType.WHITE, PatternType.STRAIT_FOUR));
  }

  @Test
  public void testHorizontalThreeMatch() {
    testSinglePattern(
        new StoneType[][]{
            {E, B, B, B, E, E},
        },
        PositionTransformer.IDENTITY,
        StoneType.BLACK,
        Patterns.get(StoneType.BLACK, PatternType.THREE));

    testSinglePattern(
        new StoneType[][]{
            {B, B, W, B, B},
            {E, E, E, W, W, W, E, B},
        },
        PositionTransformer.IDENTITY,
        StoneType.WHITE,
        Patterns.get(StoneType.WHITE, PatternType.THREE));
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
        PositionTransformer.CLOCK_90,
        StoneType.BLACK,
        Patterns.get(StoneType.BLACK, PatternType.THREE));

    testSinglePattern(
        new StoneType[][]{
            {},
            {E, W},
            {E, W},
            {E, W},
        },
        PositionTransformer.CLOCK_90,
        StoneType.WHITE,
        Patterns.get(StoneType.WHITE, PatternType.THREE));
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
        PositionTransformer.LEFT_DIAGONAL,
        StoneType.BLACK,
        Patterns.get(StoneType.BLACK, PatternType.THREE));

    testSinglePattern(
        new StoneType[][]{
            {},
            {},
            {E, E, E, W},
            {E, E, W},
            {E, W},
            {E},
        },
        PositionTransformer.RIGHT_DIAGONAL,
        StoneType.WHITE,
        Patterns.get(StoneType.WHITE, PatternType.THREE));
  }

  private void testSinglePattern(StoneType[][] board,
                                 PositionTransformer orientation,
                                 StoneType stoneType,
                                 Iterable<Pattern> candidates) {
    BoardClass boardClass = createBoard(board);
    List<Pattern> patterns = Lists.newArrayList(boardClass.filterMatchedPatterns(candidates));
    assertEquals(1, patterns.size());
    assertEquals(orientation, patterns.get(0).getTransformer());
    assertEquals(stoneType, patterns.get(0).getStoneType());
  }
}