package common;

import static common.MoveType.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import common.boardclass.BitBoard;
import model.Position;

import java.util.*;

/**
 * Predefined patterns.
 */
public class Patterns {

  private static final Map<StoneType, Map<PatternType, ImmutableSet<Pattern>>>
      PATTERNS = initializePatterns();

  private static Map<StoneType, Map<PatternType, ImmutableSet<Pattern>>> initializePatterns() {
    Map<StoneType, Map<PatternType, ImmutableSet<Pattern>>> result = new EnumMap(StoneType.class);
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE}) {
      Map<PatternType, ImmutableSet<Pattern>> innerMap = new EnumMap(PatternType.class);
      innerMap.put(PatternType.FIVE, createGoalPatterns(stoneType));
      innerMap.put(PatternType.STRAIT_FOUR, createStraitFourPatterns(stoneType));
      innerMap.put(PatternType.OPEN_FOUR, createOpenPatterns(stoneType));
      innerMap.put(PatternType.THREE, createThreePatterns(stoneType));
      result.put(stoneType, innerMap);
    }
    return result;
  }

  private static ImmutableSet<Pattern> createOpenPatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {D1, X, X, X, X, D1}))
        .build();
  }

  private static ImmutableSet<Pattern> createThreePatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {D2, X, X, D1, X, D2}))
        .addAll(createPatterns(stoneType, new MoveType[] {D2, X, D1, X, X, D2}))
        .addAll(createPatterns(stoneType, new MoveType[] {E, D1, X, X, X, D1}))
        .addAll(createPatterns(stoneType, new MoveType[] {D1, X, X, X, D1, E}))
        .build();
  }

  private static ImmutableSet<Pattern> createStraitFourPatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {D1, X, X, X, X}))
        .addAll(createPatterns(stoneType, new MoveType[] {X, D1, X, X, X}))
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, D1, X, X}))
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, X, D1, X}))
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, X, X, D1}))
        .build();
  }

  private static ImmutableSet<Pattern> createGoalPatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, X, X, X}))
        .build();
  }

  private static ImmutableList<Position> getDefensiveMoves(int i, int j, MoveType[] movePattern) {
    List<Position> defensiveMoves = new ArrayList<>();
    for (int k = 0; k < movePattern.length; k++) {
      if (movePattern[k] == MoveType.D1) {
        defensiveMoves.add(0, Position.create(i, j + k)); // insert to head
      } else if (movePattern[k] == MoveType.D2) {
        defensiveMoves.add(Position.create(i, j + k));
      }
    }
    return ImmutableList.copyOf(defensiveMoves);
  }

  private static Iterable<Pattern> createPatterns(
      StoneType stoneType,
      MoveType[] movePattern) {
    int originalPattern = generatePattern(movePattern, BitBoard.getBits(stoneType));
    int patternLength = movePattern.length;
    List<Pattern> result = new ArrayList<>();
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      int mask = (1 << (2 * patternLength)) - 1;
      int pattern = originalPattern;
      for (int j = 0; j <= Constants.BOARD_SIZE - patternLength; j++) {
        ImmutableList<Position> emptyPos = getDefensiveMoves(i, j, movePattern);
        result.add(new Pattern(i, pattern, mask, PositionTransformer.IDENTITY, stoneType, emptyPos));
        result.add(new Pattern(i, pattern, mask, PositionTransformer.CLOCK_90, stoneType,
            ImmutableList.copyOf(
                Lists.transform(emptyPos, (p) -> p.transform(PositionTransformer.CLOCK_270)))));
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
            ImmutableList.copyOf(
                Lists.transform(emptyPos, (p) -> p.transform(PositionTransformer.RIGHT_DIAGONAL_REVERSE)))));
        result.add(new Pattern(i, pattern, mask, PositionTransformer.LEFT_DIAGONAL, stoneType,
            ImmutableList.copyOf(
                Lists.transform(emptyPos, (p) -> p.transform(PositionTransformer.LEFT_DIAGONAL_REVERSE)))));
        mask <<= 2;
        pattern <<= 2;
      }
    }
    return result;
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

  public static ImmutableSet<Pattern> get(StoneType stoneType, PatternType patternType) {
    return PATTERNS.get(stoneType).get(patternType);
  }
}
