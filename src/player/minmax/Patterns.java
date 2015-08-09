package player.minmax;

import static player.minmax.MoveType.*;

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

  public static final ImmutableList<Pattern> BLACK_GOALS = createGoalPatterns(Square.BLACK_PIECE);
  public static final ImmutableList<Pattern> WHITE_GOALS = createGoalPatterns(Square.WHITE_PIECE);
  public static final ImmutableList<Pattern> BLACK_STRAIT_FOUR = createStraitFourPatterns(Square.BLACK_PIECE);
  public static final ImmutableList<Pattern> WHITE_STRAIT_FOUR = createStraitFourPatterns(Square.WHITE_PIECE);
  public static final ImmutableList<Pattern> BLACK_OPEN_FOUR = createStraitOpenPatterns(Square.BLACK_PIECE);
  public static final ImmutableList<Pattern> WHITE_OPEN_FOUR = createStraitOpenPatterns(Square.WHITE_PIECE);
  public static final ImmutableList<Pattern> BLACK_THREE = createThreePatterns(Square.BLACK_PIECE);
  public static final ImmutableList<Pattern> WHITE_THREE = createThreePatterns(Square.WHITE_PIECE);


  private static ImmutableList<Pattern> createStraitOpenPatterns(Square stoneType) {
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    result.addAll(createPatterns(stoneType, new MoveType[] {D1, X, X, X, X, D1}));
    return result.build();
  }

  private static ImmutableList<Pattern> createThreePatterns(Square stoneType) {
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    result.addAll(createPatterns(stoneType, new MoveType[] {D2, X, X, D1, X, D2}));
    result.addAll(createPatterns(stoneType, new MoveType[] {D2, X, D1, X, X, D2}));
    result.addAll(createPatterns(stoneType, new MoveType[] {E, D1, X, X, X, D1}));
    result.addAll(createPatterns(stoneType, new MoveType[] {D1, X, X, X, D1, E}));
    return result.build();
  }

  private static ImmutableList<Pattern> createStraitFourPatterns(Square stoneType) {
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    result.addAll(createPatterns(stoneType, new MoveType[] {D1, X, X, X, X}));
    result.addAll(createPatterns(stoneType, new MoveType[] {X, D1, X, X, X}));
    result.addAll(createPatterns(stoneType, new MoveType[] {X, X, D1, X, X}));
    result.addAll(createPatterns(stoneType, new MoveType[] {X, X, X, D1, X}));
    result.addAll(createPatterns(stoneType, new MoveType[] {X, X, X, X, D1}));
    return result.build();
  }

  private static ImmutableList<Pattern> createGoalPatterns(Square stoneType) {
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
      Square stoneType,
      MoveType[] movePattern) {
    int originalPattern = generatePattern(movePattern, BitBoard.getBits(stoneType));
    int patternLength = movePattern.length;
    ImmutableList.Builder<Pattern> result = ImmutableList.builder();
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      int mask = (1 << (2 * patternLength)) - 1;
      int pattern = originalPattern;
      for (int j = 0; j <= Constants.BOARD_SIZE - patternLength; j++) {
        List<Position> emptyPos = getDefensiveMoves(i, j, movePattern);
        result.add(new Pattern(i, pattern, mask, IDENTITY, stoneType, emptyPos));
        result.add(new Pattern(i, pattern, mask, CLOCK_90, stoneType,
            Lists.transform(emptyPos, (p) -> p.transform(CLOCK_270))));
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

  public static Iterable<Pattern> getThree(Square stoneType) {
    switch (stoneType) {
      case WHITE_PIECE:
        return WHITE_THREE;
      case BLACK_PIECE:
        return BLACK_THREE;
      default:
        throw new IllegalArgumentException("Invalid stone type: " + stoneType);
    }
  }

  public static Iterable<Pattern> getStraitFour(Square stoneType) {
    switch (stoneType) {
      case WHITE_PIECE:
        return WHITE_STRAIT_FOUR;
      case BLACK_PIECE:
        return BLACK_STRAIT_FOUR;
      default:
        throw new IllegalArgumentException("Invalid stone type: " + stoneType);
    }
  }

  public static Iterable<Pattern> getThreatPatterns(Square stoneType) {
    return Iterables.concat(getThree(stoneType), getStraitFour(stoneType));
  }
}
