package player.minmax;

import com.google.common.collect.ImmutableList;
import common.Constants;
import common.Square;

import java.util.ArrayList;
import java.util.List;

import static player.minmax.PositionTransformer.*;

/**
 * Predefined patterns.
 */
public class Patterns {

  private static final Square E = Square.NOTHING;

  public static final ImmutableList<Pattern> BLACK_GOALS = createGoalPatterns(Square.BLACK_PIECE);
  public static final ImmutableList<Pattern> WHITE_GOALS = createGoalPatterns(Square.WHITE_PIECE);
  public static final ImmutableList<Pattern> BLACK_STRAIT_FOUR = createStraitFourPatterns(Square.BLACK_PIECE);
  public static final ImmutableList<Pattern> WHITE_STRAIT_FOUR = createStraitFourPatterns(Square.WHITE_PIECE);
  public static final ImmutableList<Pattern> BLACK_BROKEN_THREE = createBrokenThreePatterns(Square.BLACK_PIECE);
  public static final ImmutableList<Pattern> WHITE_BROKEN_THREE = createBrokenThreePatterns(Square.WHITE_PIECE);
  public static final ImmutableList<Pattern> BLACK_THREE = createThreePatterns(Square.BLACK_PIECE);
  public static final ImmutableList<Pattern> WHITE_THREE = createThreePatterns(Square.WHITE_PIECE);

  public static final ImmutableList<Pattern> ALL_PATTERNS =
      ImmutableList.<Pattern>builder()
          .addAll(BLACK_GOALS)
          .addAll(WHITE_GOALS)
          .addAll(BLACK_STRAIT_FOUR)
          .addAll(WHITE_STRAIT_FOUR)
          .addAll(BLACK_BROKEN_THREE)
          .addAll(WHITE_BROKEN_THREE)
          .addAll(BLACK_THREE)
          .addAll(WHITE_THREE)
          .build();

  private static ImmutableList<Pattern> createThreePatterns(Square X) {
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    result.addAll(createPatterns(X, new Square[] {E, E, X, X, X, E}));
    result.addAll(createPatterns(X, new Square[] {E, X, X, X, E, E}));
    return result.build();
  }

  private static ImmutableList<Pattern> createBrokenThreePatterns(Square X) {
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    result.addAll(createPatterns(X, new Square[] {E, X, X, E, X, E}));
    result.addAll(createPatterns(X, new Square[] {E, X, E, X, X, E}));
    return result.build();
  }

  private static ImmutableList<Pattern> createStraitFourPatterns(Square X) {
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    result.addAll(createPatterns(X, new Square[] {E, X, X, X, X}));
    result.addAll(createPatterns(X, new Square[] {X, X, X, X, E}));
    return result.build();
  }

  private static ImmutableList<Pattern> createGoalPatterns(Square X) {
    return createPatterns(X, new Square[]{X, X, X, X, X});
  }

  private static ImmutableList<Pattern> createPatterns(
      Square stoneType,
      Square[] squarePattern) {
    int originalPattern = generatePattern(squarePattern);
    int patternLength = squarePattern.length;
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      int mask = (1 << (2 * patternLength)) - 1;
      int pattern = originalPattern;
      for (int j = 0; j <= Constants.BOARD_SIZE - patternLength; j++) {
        result.add(new Pattern(i, pattern, mask, IDENTITY, stoneType));
        result.add(new Pattern(i, pattern, mask, CLOCK_90, stoneType));
        mask <<= 2;
        pattern <<= 2;
      }
    }
    for (int i = 0; i < Constants.BOARD_SIZE * 2 - 1; i++) {
      int mask = (1 << (2 * patternLength)) - 1;
      int pattern = originalPattern;
      int maxCol = Math.min(i + 1, 2 * Constants.BOARD_SIZE - 1 - i);
      for (int j = 0; j <= maxCol - patternLength; j++) {
        result.add(new Pattern(i, pattern, mask, RIGHT_DIAGONAL, stoneType));
        result.add(new Pattern(i, pattern, mask, LEFT_DIAGONAL, stoneType));
        mask <<= 2;
        pattern <<= 2;
      }
    }
    return result.build();
  }

  private static int generatePattern(Square[] squares) {
    int pattern = 0;
    for (Square square : squares) {
      pattern <<= 2;
      pattern |= BitBoard.getBits(square);
    }
    return pattern;
  }
}
