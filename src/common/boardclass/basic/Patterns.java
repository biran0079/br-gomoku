package common.boardclass.basic;

import static common.pattern.MoveType.D;
import static common.pattern.MoveType.E;
import static common.pattern.MoveType.X;
import static common.pattern.PatternsUtil.createPatterns;

import com.google.common.collect.ImmutableSet;

import common.PositionTransformer;
import common.StoneType;
import common.pattern.MoveType;
import common.pattern.PatternType;
import common.pattern.PatternsUtil;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import model.Position;

/**
 * Predefined patterns.
 * Index included.
 */
class Patterns {

  private final Map<StoneType, Map<Position, Set<PatternImpl>>> index;
  private final Map<StoneType, Map<PatternType, ImmutableSet<PatternImpl>>> patterns;

  Patterns() {
    this.index = PatternsUtil.emptyIndex();
    this.patterns = initializePatterns();
  }

  ImmutableSet<PatternImpl> get(StoneType stoneType, PatternType patternType) {
    return patterns.get(stoneType).get(patternType);
  }

  Set<PatternImpl> get(int i, int j, StoneType stoneType) {
    return index.get(stoneType).get(Position.of(i, j));
  }

  private Map<StoneType, Map<PatternType, ImmutableSet<PatternImpl>>>
      initializePatterns() {
    Map<StoneType, Map<PatternType, ImmutableSet<PatternImpl>>> result =
        new EnumMap<>(StoneType.class);
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE}) {
      Map<PatternType, ImmutableSet<PatternImpl>> innerMap = new EnumMap<>(PatternType.class);
      innerMap.put(PatternType.GOAL, createGoalPatterns(stoneType));
      innerMap.put(PatternType.FIVE, innerMap.get(PatternType.GOAL));
      innerMap.put(PatternType.FOUR, createFourPatterns(stoneType));
      innerMap.put(PatternType.STRAIT_FOUR, createStraitFourPatterns(stoneType));
      innerMap.put(PatternType.THREE, createThreePatterns(stoneType));
      result.put(stoneType, innerMap);
    }
    return result;
  }

  private PatternImpl createPattern(int i, int j, int pattern, int mask,
                        PositionTransformer transformer, StoneType stoneType,
                        MoveType[] movePattern) {
    PatternImpl result = new PatternImpl(i, pattern, mask, transformer, stoneType,
        PatternsUtil.getDefensiveMoves(i, j, movePattern, transformer.reverse()));
    PositionTransformer reverseTransform = transformer.reverse();
    for (int k = 0; k < movePattern.length; k++) {
      if (movePattern[k] == MoveType.X) {
        Position p = Position.of(i, j + k).transform(reverseTransform);
        index.get(stoneType).get(p).add(result);
      }
    }
    return result;
  }

  private ImmutableSet<PatternImpl> createGoalPatterns(StoneType stoneType) {
    return ImmutableSet.<PatternImpl>builder()
        .addAll(createPatterns(stoneType, new MoveType[]{X, X, X, X, X}, this::createPattern))
        .build();
  }

  private ImmutableSet<PatternImpl> createStraitFourPatterns(StoneType stoneType) {
    return ImmutableSet.<PatternImpl>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {D, X, X, X, X, D}, this::createPattern))
        .build();
  }

  private ImmutableSet<PatternImpl> createThreePatterns(StoneType stoneType) {
    return ImmutableSet.<PatternImpl>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {D, X, X, D, X, D}, this::createPattern))
        .addAll(createPatterns(stoneType, new MoveType[] {D, X, D, X, X, D}, this::createPattern))
        .addAll(createPatterns(stoneType, new MoveType[] {E, D, X, X, X, D}, this::createPattern))
        .addAll(createPatterns(stoneType, new MoveType[] {D, X, X, X, D, E}, this::createPattern))
        .build();
  }

  private ImmutableSet<PatternImpl> createFourPatterns(StoneType stoneType) {
    return ImmutableSet.<PatternImpl>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {D, X, X, X, X}, this::createPattern))
        .addAll(createPatterns(stoneType, new MoveType[] {X, D, X, X, X}, this::createPattern))
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, D, X, X}, this::createPattern))
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, X, D, X}, this::createPattern))
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, X, X, D}, this::createPattern))
        .build();
  }
}
