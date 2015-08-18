package common.boardclass.threatbased;

import static common.pattern.MoveType.D;
import static common.pattern.MoveType.E;
import static common.pattern.MoveType.O;
import static common.pattern.MoveType.X;
import static common.pattern.PatternsUtil.createPatterns;
import static common.pattern.PatternsUtil.getDefensiveMoves;
import static common.pattern.PatternsUtil.getOffensiveMove;

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
 * Default threat factory.
 * Index included.
 */
class Threats {

  private final Map<StoneType, Map<PatternType, ImmutableSet<ThreatImpl>>> threats;
  private final Map<StoneType, Map<Position, Set<ThreatImpl>>> index;

  Threats() {
    this.index = PatternsUtil.emptyIndex();
    this.threats = initializeThreats();
  }

  ImmutableSet<ThreatImpl> get(StoneType stoneType, PatternType PatternType) {
    return threats.get(stoneType).get(PatternType);
  }

  Set<ThreatImpl> get(int i, int j, StoneType stoneType) {
    return index.get(stoneType).get(Position.of(i, j));
  }

  private Map<StoneType, Map<PatternType, ImmutableSet<ThreatImpl>>> initializeThreats() {
    Map<StoneType, Map<PatternType, ImmutableSet<ThreatImpl>>> result =
        new EnumMap<>(StoneType.class);
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE}) {
      Map<PatternType, ImmutableSet<ThreatImpl>> innerMap = new EnumMap<>(PatternType.class);
      innerMap.put(PatternType.GOAL, createGoalThreats(stoneType));
      innerMap.put(PatternType.FIVE, createFiveThreats(stoneType));
      innerMap.put(PatternType.STRAIT_FOUR, createStraitFourThreats(stoneType));
      innerMap.put(PatternType.FOUR, createFourThreats(stoneType));
      innerMap.put(PatternType.THREE, createThreeWithThreats(stoneType));
      result.put(stoneType, innerMap);
    }
    return result;
  }

  private ImmutableSet<ThreatImpl> createGoalThreats(StoneType stoneType) {
    return ImmutableSet.<ThreatImpl>builder()
      .addAll(createPatterns(stoneType, new MoveType[] {X, X, X, X, X}, this::createThreat))
      .build();
  }

  private ImmutableSet<ThreatImpl> createFiveThreats(StoneType stoneType) {
    ImmutableSet.Builder<ThreatImpl> builder = ImmutableSet.builder();
    for (int i = 0; i < 5; i++) {
      MoveType[] moves = new MoveType[] {X, X, X, X, X};
      moves[i] = O;
      builder.addAll(createPatterns(stoneType, moves, this::createThreat));
    }
    return builder.build();
  }

  private ImmutableSet<ThreatImpl> createStraitFourThreats(StoneType stoneType) {
    ImmutableSet.Builder<ThreatImpl> builder = ImmutableSet.builder();
    for (int i = 1; i < 5; i++) {
      MoveType[] moves = new MoveType[] {E, X, X, X, X, E};
      moves[i] = O;
      builder.addAll(createPatterns(stoneType, moves, this::createThreat));
    }
    return builder.build();
  }

  private ImmutableSet<ThreatImpl> createFourThreats(StoneType stoneType) {
    ImmutableSet.Builder<ThreatImpl> builder = ImmutableSet.builder();
    for (int i = 0; i < 5; i++) {
      for (int j = i + 1; j < 5; j++) {
        MoveType[] moves = new MoveType[] {X, X, X, X, X};
        moves[i] = D;
        moves[j] = O;
        builder.addAll(createPatterns(stoneType, moves, this::createThreat));
        moves[i] = O;
        moves[j] = D;
        builder.addAll(createPatterns(stoneType, moves, this::createThreat));
      }
    }
    return builder.build();
  }

  private ImmutableSet<ThreatImpl> createThreeWithThreats(StoneType stoneType) {
    ImmutableSet.Builder<ThreatImpl> builder = ImmutableSet.builder();
    for (int i = 1; i < 5; i++) {
      for (int j = i + 1; j < 5; j++) {
        MoveType[] moves = new MoveType[] {D, X, X, X, X, D};
        moves[i] = D;
        moves[j] = O;
        builder.addAll(createPatterns(stoneType, moves, this::createThreat));

        moves[i] = O;
        moves[j] = X;
        builder.addAll(createPatterns(stoneType, moves, this::createThreat));
      }
    }
    for (int i = 2; i < 5; i++) {
      MoveType[] moves = new MoveType[] {E, D, X, X, X, D, E};
      moves[i] = O;
      builder.addAll(createPatterns(stoneType, moves, this::createThreat));
    }
    return builder.build();
  }

  private ThreatImpl createThreat(int i, int j, int pattern, int mask,
                      PositionTransformer transformer, StoneType stoneType,
                      MoveType[] movePattern) {
    ThreatImpl result = new ThreatImpl(i, pattern, mask, transformer, stoneType,
        getDefensiveMoves(i, j, movePattern, transformer.reverse()),
        getOffensiveMove(i, j, movePattern, transformer.reverse()));
    PositionTransformer reverseTransform = transformer.reverse();
    for (int k = 0; k < movePattern.length; k++) {
      if (movePattern[k] == MoveType.X) {
        Position p = Position.of(i, j + k).transform(reverseTransform);
        index.get(stoneType).get(p).add(result);
      }
    }
    return result;
  }
}
