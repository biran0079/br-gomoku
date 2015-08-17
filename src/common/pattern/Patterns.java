package common.pattern;

import static common.pattern.MoveType.D;
import static common.pattern.MoveType.E;
import static common.pattern.MoveType.X;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import common.Constants;
import common.PatternType;
import common.PositionTransformer;
import common.StoneType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import model.Position;

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
      innerMap.put(PatternType.FIVE, createFivePatterns(stoneType));
      innerMap.put(PatternType.FOUR, createFourPatterns(stoneType));
      innerMap.put(PatternType.STRAIT_FOUR, createStraitFourPatterns(stoneType));
      innerMap.put(PatternType.THREE, createThreePatterns(stoneType));
      result.put(stoneType, innerMap);
    }
    return result;
  }

  Pattern createPattern(int i, int j, int pattern, int mask,
                        PositionTransformer transformer, StoneType stoneType,
                        MoveType[] movePattern) {
    return new PatternImpl(i, pattern, mask, transformer, stoneType,
        PatternsUtil.getDefensiveMoves(i, j, movePattern, transformer.reverse()));
  }

  private ImmutableSet<Pattern> createStraitFourPatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {D, X, X, X, X, D}))
        .build();
  }

  private ImmutableSet<Pattern> createThreePatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {D, X, X, D, X, D}))
        .addAll(createPatterns(stoneType, new MoveType[] {D, X, D, X, X, D}))
        .addAll(createPatterns(stoneType, new MoveType[] {E, D, X, X, X, D}))
        .addAll(createPatterns(stoneType, new MoveType[] {D, X, X, X, D, E}))
        .build();
  }

  private ImmutableSet<Pattern> createFourPatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {D, X, X, X, X}))
        .addAll(createPatterns(stoneType, new MoveType[] {X, D, X, X, X}))
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, D, X, X}))
        .addAll(createPatterns(stoneType, new MoveType[]{X, X, X, D, X}))
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, X, X, D}))
        .build();
  }

  private ImmutableSet<Pattern> createFivePatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, X, X, X}))
        .build();
  }

  private Iterable<Pattern> createPatterns(
      StoneType stoneType,
      MoveType[] movePattern) {
    int originalPattern = PatternsUtil.generatePattern(movePattern, stoneType.getBits());
    int patternLength = movePattern.length;
    List<Pattern> result = new ArrayList<>();
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      int mask = (1 << (2 * patternLength)) - 1;
      int pattern = originalPattern;
      for (int j = 0; j <= Constants.BOARD_SIZE - patternLength; j++) {
        result.add(createPattern(i, j, pattern, mask, PositionTransformer.IDENTITY,
            stoneType, movePattern));
        result.add(createPattern(i, j, pattern, mask, PositionTransformer.CLOCK_90,
            stoneType, movePattern));
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
            stoneType, movePattern));
        result.add(createPattern(i, j, pattern, mask, PositionTransformer.LEFT_DIAGONAL,
            stoneType, movePattern));
        mask <<= 2;
        pattern <<= 2;
      }
    }
    return result;
  }

  public ImmutableSet<Pattern> get(StoneType stoneType, PatternType patternType) {
    return patterns.get(stoneType).get(patternType);
  }
}
