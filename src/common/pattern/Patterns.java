package common.pattern;

import static common.pattern.MoveType.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import common.Constants;
import common.PatternType;
import common.PositionTransformer;
import common.StoneType;
import model.Position;

import java.util.*;

/**
 * Predefined patterns.
 */
class Patterns implements Pattern.Factory {

  private final Map<StoneType, Map<PatternType, ImmutableSet<Pattern>>> patterns;

  Patterns() {
    this.patterns = initializePatterns();
  }

  Map<StoneType, Map<PatternType, ImmutableSet<Pattern>>> initializePatterns() {
    Map<StoneType, Map<PatternType, ImmutableSet<Pattern>>> result = new EnumMap(StoneType.class);
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE}) {
      Map<PatternType, ImmutableSet<Pattern>> innerMap = new EnumMap(PatternType.class);
      innerMap.put(PatternType.FIVE, createGoalPatterns(stoneType));
      innerMap.put(PatternType.FOUR, createFourPatterns(stoneType));
      innerMap.put(PatternType.STRAIT_FOUR, createStraitFourPatterns(stoneType));
      innerMap.put(PatternType.THREE, createThreePatterns(stoneType));
      result.put(stoneType, innerMap);
    }
    return result;
  }

  Pattern createPattern(int i, int j, int pattern, int mask,
                        PositionTransformer transformer, StoneType stoneType,
                        MoveType[] movePattern, PatternType patternType) {
    return new PatternImpl(i, pattern, mask, transformer, stoneType,
        getDefensiveMoves(i, j, movePattern, transformer.reverse()), patternType);
  }

  private ImmutableSet<Pattern> createStraitFourPatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {D1, X, X, X, X, D1}, PatternType.STRAIT_FOUR))
        .build();
  }

  private ImmutableSet<Pattern> createThreePatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {D2, X, X, D1, X, D2}, PatternType.THREE))
        .addAll(createPatterns(stoneType, new MoveType[] {D2, X, D1, X, X, D2}, PatternType.THREE))
        .addAll(createPatterns(stoneType, new MoveType[] {E, D1, X, X, X, D1}, PatternType.THREE))
        .addAll(createPatterns(stoneType, new MoveType[] {D1, X, X, X, D1, E}, PatternType.THREE))
        .build();
  }

  private ImmutableSet<Pattern> createFourPatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {D1, X, X, X, X}, PatternType.FOUR))
        .addAll(createPatterns(stoneType, new MoveType[] {X, D1, X, X, X}, PatternType.FOUR))
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, D1, X, X}, PatternType.FOUR))
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, X, D1, X}, PatternType.FOUR))
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, X, X, D1}, PatternType.FOUR))
        .build();
  }

  private ImmutableSet<Pattern> createGoalPatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, X, X, X}, PatternType.FIVE))
        .build();
  }

  private static ImmutableList<Position> getDefensiveMoves(int i, int j, MoveType[] movePattern,
      PositionTransformer transformer) {
    List<Position> defensiveMoves = new ArrayList<>();
    for (int k = 0; k < movePattern.length; k++) {
      Position p = Position.create(i, j + k).transform(transformer);
      if (movePattern[k] == MoveType.D1) {
        defensiveMoves.add(0, p); // insert to head
      } else if (movePattern[k] == MoveType.D2) {
        defensiveMoves.add(p);
      }
    }
    return ImmutableList.copyOf(defensiveMoves);
  }

  private Iterable<Pattern> createPatterns(
      StoneType stoneType,
      MoveType[] movePattern,
      PatternType patternType) {
    int originalPattern = generatePattern(movePattern, stoneType.getBits());
    int patternLength = movePattern.length;
    List<Pattern> result = new ArrayList<>();
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      int mask = (1 << (2 * patternLength)) - 1;
      int pattern = originalPattern;
      for (int j = 0; j <= Constants.BOARD_SIZE - patternLength; j++) {
        result.add(createPattern(i, j, pattern, mask, PositionTransformer.IDENTITY,
            stoneType, movePattern, patternType));
        result.add(createPattern(i, j, pattern, mask, PositionTransformer.CLOCK_90,
            stoneType, movePattern, patternType));
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
        result.add(createPattern(i, j, pattern, mask, PositionTransformer.RIGHT_DIAGONAL,
            stoneType, movePattern, patternType));
        result.add(createPattern(i, j, pattern, mask, PositionTransformer.LEFT_DIAGONAL,
            stoneType, movePattern, patternType));
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

  public ImmutableSet<Pattern> get(StoneType stoneType, PatternType patternType) {
    return patterns.get(stoneType).get(patternType);
  }
}
