package common;

import static common.MoveType.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Predefined patterns.
 */
public class Patterns {

  public static final ImmutableList<Pattern> BLACK_GOALS = createGoalPatterns(StoneType.BLACK);
  public static final ImmutableList<Pattern> WHITE_GOALS = createGoalPatterns(StoneType.WHITE);
  public static final ImmutableList<Pattern> BLACK_STRAIT_FOUR = createStraitFourPatterns(StoneType.BLACK);
  public static final ImmutableList<Pattern> WHITE_STRAIT_FOUR = createStraitFourPatterns(StoneType.WHITE);
  public static final ImmutableList<Pattern> BLACK_OPEN_FOUR = createStraitOpenPatterns(StoneType.BLACK);
  public static final ImmutableList<Pattern> WHITE_OPEN_FOUR = createStraitOpenPatterns(StoneType.WHITE);
  public static final ImmutableList<Pattern> BLACK_THREE = createThreePatterns(StoneType.BLACK);
  public static final ImmutableList<Pattern> WHITE_THREE = createThreePatterns(StoneType.WHITE);


  private static ImmutableList<Pattern> createStraitOpenPatterns(StoneType stoneType) {
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    result.addAll(createPatterns(stoneType, new MoveType[] {D1, X, X, X, X, D1}));
    return result.build();
  }

  private static ImmutableList<Pattern> createThreePatterns(StoneType stoneType) {
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    result.addAll(createPatterns(stoneType, new MoveType[] {D2, X, X, D1, X, D2}));
    result.addAll(createPatterns(stoneType, new MoveType[] {D2, X, D1, X, X, D2}));
    result.addAll(createPatterns(stoneType, new MoveType[] {E, D1, X, X, X, D1}));
    result.addAll(createPatterns(stoneType, new MoveType[] {D1, X, X, X, D1, E}));
    return result.build();
  }

  private static ImmutableList<Pattern> createStraitFourPatterns(StoneType stoneType) {
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    result.addAll(createPatterns(stoneType, new MoveType[] {D1, X, X, X, X}));
    result.addAll(createPatterns(stoneType, new MoveType[] {X, D1, X, X, X}));
    result.addAll(createPatterns(stoneType, new MoveType[] {X, X, D1, X, X}));
    result.addAll(createPatterns(stoneType, new MoveType[] {X, X, X, D1, X}));
    result.addAll(createPatterns(stoneType, new MoveType[] {X, X, X, X, D1}));
    return result.build();
  }

  private static ImmutableList<Pattern> createGoalPatterns(StoneType stoneType) {
    return createPatterns(stoneType, new MoveType[]{X, X, X, X, X});
  }

  private static List<Position> getDefensiveMoves(int i, int j, MoveType[] movePattern) {
    List<Position> defensiveMoves = new ArrayList<>();
    for (int k = 0; k < movePattern.length; k++) {
      if (movePattern[k] == MoveType.D1) {
        defensiveMoves.add(0, Position.create(i, j + k)); // insert to head
      } else if (movePattern[k] == MoveType.D2) {
        defensiveMoves.add(Position.create(i, j + k));
      }
    }
    return defensiveMoves;
  }

  private static ImmutableList<Pattern> createPatterns(
      StoneType stoneType,
      MoveType[] movePattern) {
    int originalPattern = generatePattern(movePattern, BitBoard.getBits(stoneType));
    int patternLength = movePattern.length;
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      int mask = (1 << (2 * patternLength)) - 1;
      int pattern = originalPattern;
      for (int j = 0; j <= Constants.BOARD_SIZE - patternLength; j++) {
        List<Position> emptyPos = getDefensiveMoves(i, j, movePattern);
        result.add(new Pattern(i, pattern, mask, PositionTransformer.IDENTITY, stoneType, emptyPos));
        result.add(new Pattern(i, pattern, mask, PositionTransformer.CLOCK_90, stoneType,
            Lists.transform(emptyPos, (p) -> p.transform(PositionTransformer.CLOCK_270))));
        mask <<= 2;
        pattern <<= 2;
      }
    }
    for (int i = 0; i < Constants.BOARD_SIZE * 2 - 1; i++) {
      int start = Math.max(0, i - Constants.BOARD_SIZE + 1);
      int maxCol = Math.min(i + 1, Constants.BOARD_SIZE);
      int pattern = originalPattern << (2 * start);
      int mask = ((1 << (2 * patternLength)) - 1) << (2 * start);
      for (int j = start; j <= maxCol - patternLength; j++) {
        List<Position> emptyPos = getDefensiveMoves(i, j, movePattern);
        result.add(new Pattern(i, pattern, mask, PositionTransformer.RIGHT_DIAGONAL, stoneType,
            Lists.transform(emptyPos, (p) -> p.transform(PositionTransformer.RIGHT_DIAGONAL_REVERSE))));
        result.add(new Pattern(i, pattern, mask, PositionTransformer.LEFT_DIAGONAL, stoneType,
            Lists.transform(emptyPos, (p) -> p.transform(PositionTransformer.LEFT_DIAGONAL_REVERSE))));
        mask <<= 2;
        pattern <<= 2;
      }
    }
    return result.build();
  }

  private static int generatePattern(MoveType[] moves, int bits) {
    int pattern = 0;
    int i = 0;
    for (MoveType move : moves) {
      if (move == MoveType.X) {
        pattern |= (bits << i);
      }
      i += 2;
    }
    return pattern;
  }

  public static Iterable<Pattern> getThree(StoneType stoneType) {
    switch (stoneType) {
      case WHITE:
        return WHITE_THREE;
      case BLACK:
        return BLACK_THREE;
      default:
        throw new IllegalArgumentException("Invalid stone type: " + stoneType);
    }
  }

  public static Iterable<Pattern> getStraitFour(StoneType stoneType) {
    switch (stoneType) {
      case WHITE:
        return WHITE_STRAIT_FOUR;
      case BLACK:
        return BLACK_STRAIT_FOUR;
      default:
        throw new IllegalArgumentException("Invalid stone type: " + stoneType);
    }
  }

  public static Iterable<Pattern> getGoalPatterns(StoneType stoneType) {
    switch (stoneType) {
      case WHITE:
        return WHITE_GOALS;
      case BLACK:
        return BLACK_GOALS;
      default:
        throw new IllegalArgumentException("Invalid stone type: " + stoneType);
    }
  }
}
