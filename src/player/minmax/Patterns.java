package player.minmax;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import common.Constants;
import common.Square;
import model.Position;

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
  public static final ImmutableList<Pattern> BLACK_OPEN_FOUR = createStraitOpenPatterns(Square.BLACK_PIECE);
  public static final ImmutableList<Pattern> WHITE_OPEN_FOUR = createStraitOpenPatterns(Square.WHITE_PIECE);
  public static final ImmutableList<Pattern> BLACK_THREE = createThreePatterns(Square.BLACK_PIECE);
  public static final ImmutableList<Pattern> WHITE_THREE = createThreePatterns(Square.WHITE_PIECE);


  private static ImmutableList<Pattern> createStraitOpenPatterns(Square X) {
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    result.addAll(createPatterns(X, new Square[] {E, X, X, X, X, E}));
    return result.build();
  }

  private static ImmutableList<Pattern> createThreePatterns(Square X) {
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    result.addAll(createPatterns(X, new Square[] {E, X, X, E, X, E}));
    result.addAll(createPatterns(X, new Square[] {E, X, E, X, X, E}));
    result.addAll(createPatterns(X, new Square[] {E, E, X, X, X, E}));
    result.addAll(createPatterns(X, new Square[] {E, X, X, X, E, E}));
    return result.build();
  }

  private static ImmutableList<Pattern> createStraitFourPatterns(Square X) {
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    result.addAll(createPatterns(X, new Square[] {E, X, X, X, X}));
    result.addAll(createPatterns(X, new Square[] {X, E, X, X, X}));
    result.addAll(createPatterns(X, new Square[] {X, X, E, X, X}));
    result.addAll(createPatterns(X, new Square[] {X, X, X, E, X}));
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
        List<Position> emptyPos = new ArrayList<>();
        for (int k = 0; k < patternLength; k++) {
          if (squarePattern[k] == Square.NOTHING) {
            emptyPos.add(Position.create(i, j + k));
          }
        }
        result.add(new Pattern(i, pattern, mask, IDENTITY, stoneType, emptyPos));
        result.add(new Pattern(i, pattern, mask, CLOCK_90, stoneType,
            Lists.transform(emptyPos, (p) -> p.transform(CLOCK_270))));
        mask <<= 2;
        pattern <<= 2;
      }
    }
    for (int i = 0; i < Constants.BOARD_SIZE * 2 - 1; i++) {
      int mask = (1 << (2 * patternLength)) - 1;
      int pattern = originalPattern;
      int maxCol = Math.min(i + 1, 2 * Constants.BOARD_SIZE - 1 - i);
      for (int j = 0; j <= maxCol - patternLength; j++) {
        List<Position> emptyPos = new ArrayList<>();
        for (int k = 0; k < patternLength; k++) {
          if (squarePattern[k] == Square.NOTHING) {
            emptyPos.add(Position.create(i, j + k));
          }
        }
        result.add(new Pattern(i, pattern, mask, RIGHT_DIAGONAL, stoneType,
            Lists.transform(emptyPos, (p) -> p.transform(RIGHT_DIAGONAL_REVERSE))));
        result.add(new Pattern(i, pattern, mask, LEFT_DIAGONAL, stoneType,
            Lists.transform(emptyPos, (p) -> p.transform(LEFT_DIAGONAL_REVERSE))));
        mask <<= 2;
        pattern <<= 2;
      }
    }
    return result.build();
  }

  private static int generatePattern(Square[] squares) {
    int pattern = 0;
    int i = 0;
    for (Square square : squares) {
      pattern |= (BitBoard.getBits(square) << i);
      i += 2;
    }
    return pattern;
  }

  public static Iterable<Pattern> getThreatPatterns(Square stoneType) {
    switch (stoneType) {
      case WHITE_PIECE:
        return Iterables.concat(WHITE_STRAIT_FOUR, WHITE_THREE, WHITE_OPEN_FOUR);
      case BLACK_PIECE:
        return Iterables.concat(BLACK_STRAIT_FOUR, BLACK_THREE, BLACK_OPEN_FOUR);
      default:
        throw new IllegalArgumentException("Invalid stone type: " + stoneType);
    }
  }
}
