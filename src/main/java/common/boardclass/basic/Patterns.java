package common.boardclass.basic;

import static common.pattern.PatternType.*;

import com.google.common.collect.ImmutableSet;
import common.PositionTransformer;
import common.StoneType;
import common.pattern.MoveType;
import common.pattern.Pattern;
import common.pattern.PatternType;
import common.pattern.PatternsUtil;
import model.Position;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import static common.pattern.MoveType.*;
import static common.pattern.PatternsUtil.createPatterns;

/**
 * Predefined patterns.
 * Index included.
 */
class Patterns implements Pattern.Corpus<Pattern> {

  private final Map<StoneType, Map<Position, Set<Pattern>>> index;
  private final Map<StoneType, Map<PatternType, ImmutableSet<Pattern>>> patterns;

  Patterns() {
    this.index = PatternsUtil.emptyIndex();
    this.patterns = initializePatterns();
  }

  @Override
  public ImmutableSet<Pattern> get(StoneType stoneType, PatternType patternType) {
    return patterns.get(stoneType).get(patternType);
  }

  @Override
  public Set<Pattern> get(int i, int j, StoneType stoneType) {
    return index.get(stoneType).get(Position.of(i, j));
  }

  private Map<StoneType, Map<PatternType, ImmutableSet<Pattern>>>
      initializePatterns() {
    Map<StoneType, Map<PatternType, ImmutableSet<Pattern>>> result =
        new EnumMap<>(StoneType.class);
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE}) {
      Map<PatternType, ImmutableSet<Pattern>> innerMap = new EnumMap<>(PatternType.class);
      innerMap.put(PatternType.GOAL, createGoalPatterns(stoneType));
      innerMap.put(PatternType.FIVE, createFivePatterns(stoneType));
      innerMap.put(PatternType.FOUR, createFourPatterns(stoneType));
      innerMap.put(PatternType.STRAIT_FOUR, createStraitFourPatterns(stoneType));
      innerMap.put(PatternType.THREE, createThreePatterns(stoneType));
      result.put(stoneType, innerMap);
    }
    return result;
  }

  private Pattern createPattern(int i, int j, int pattern, int mask,
                                PositionTransformer transformer, StoneType stoneType,
                                MoveType[] movePattern, PatternType patternType) {
    Pattern result = new PatternImpl(i, pattern, mask, transformer, stoneType,
        PatternsUtil.getDefensiveMoves(i, j, movePattern, transformer.reverse()),
        patternType);
    PositionTransformer reverseTransform = transformer.reverse();
    for (int k = 0; k < movePattern.length; k++) {
      if (movePattern[k] == MoveType.X) {
        Position p = Position.of(i, j + k).transform(reverseTransform);
        index.get(stoneType).get(p).add(result);
      }
    }
    return result;
  }

  private ImmutableSet<Pattern> createGoalPatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[]{X, X, X, X, X}, GOAL, this::createPattern))
        .build();
  }

  private ImmutableSet<Pattern> createFivePatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[]{X, X, X, X, X}, FIVE, this::createPattern))
        .build();
  }

  private ImmutableSet<Pattern> createStraitFourPatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[]{D, X, X, X, X, D}, STRAIT_FOUR, this::createPattern))
        .build();
  }

  private ImmutableSet<Pattern> createThreePatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {D, X, X, D, X, D}, THREE, this::createPattern))
        .addAll(createPatterns(stoneType, new MoveType[] {D, X, D, X, X, D}, THREE, this::createPattern))
        .addAll(createPatterns(stoneType, new MoveType[] {E, D, X, X, X, D}, THREE, this::createPattern))
        .addAll(createPatterns(stoneType, new MoveType[] {D, X, X, X, D, E}, THREE, this::createPattern))
        .build();
  }

  private ImmutableSet<Pattern> createFourPatterns(StoneType stoneType) {
    return ImmutableSet.<Pattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[] {D, X, X, X, X}, FOUR, this::createPattern))
        .addAll(createPatterns(stoneType, new MoveType[] {X, D, X, X, X}, FOUR, this::createPattern))
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, D, X, X}, FOUR, this::createPattern))
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, X, D, X}, FOUR, this::createPattern))
        .addAll(createPatterns(stoneType, new MoveType[] {X, X, X, X, D}, FOUR, this::createPattern))
        .build();
  }
}
