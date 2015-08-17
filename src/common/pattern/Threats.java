package common.pattern;

import static common.pattern.PatternsUtil.getDefensiveMoves;
import static common.pattern.PatternsUtil.getOffensiveMove;
import static common.pattern.PatternsUtil.generatePattern;

import com.google.common.collect.ImmutableSet;
import common.Constants;
import common.PatternType;
import common.PositionTransformer;
import common.StoneType;
import model.Position;

import java.util.*;

import static common.pattern.MoveType.E;
import static common.pattern.MoveType.O;
import static common.pattern.MoveType.D;
import static common.pattern.MoveType.X;

/**
 * Default threat factory.
 * Index included.
 */
public class Threats implements Threat.Factory {

  private final Map<StoneType, Map<PatternType, ImmutableSet<Threat>>> threats;
  private final Map<StoneType, Map<Position, Set<Threat>>> index;

  public Threats() {
    this.index = PatternsUtil.emptyIndex();
    this.threats = initializeThreats();
  }

  Map<StoneType, Map<PatternType, ImmutableSet<Threat>>> initializeThreats() {
    Map<StoneType, Map<PatternType, ImmutableSet<Threat>>> result = new EnumMap(StoneType.class);
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE}) {
      Map<PatternType, ImmutableSet<Threat>> innerMap = new EnumMap(PatternType.class);
      innerMap.put(PatternType.FIVE, createFiveThreats(stoneType));
      innerMap.put(PatternType.STRAIT_FOUR, createStraitFourThreats(stoneType));
      innerMap.put(PatternType.FOUR, createFourThreats(stoneType));
      innerMap.put(PatternType.THREE, createThreeWithThreats(stoneType));
      result.put(stoneType, innerMap);
    }
    return result;
  }

  private ImmutableSet<Threat> createFiveThreats(StoneType stoneType) {
    ImmutableSet.Builder<Threat> builder = ImmutableSet.builder();
    for (int i = 0; i < 5; i++) {
      MoveType[] moves = new MoveType[] {X, X, X, X, X};
      moves[i] = O;
      builder.addAll(createThreats(stoneType, moves));
    }
    return builder.build();
  }

  private ImmutableSet<Threat> createStraitFourThreats(StoneType stoneType) {
    ImmutableSet.Builder<Threat> builder = ImmutableSet.builder();
    for (int i = 1; i < 5; i++) {
      MoveType[] moves = new MoveType[] {E, X, X, X, X, E};
      moves[i] = O;
      builder.addAll(createThreats(stoneType, moves));
    }
    return builder.build();
  }

  private ImmutableSet<Threat> createFourThreats(StoneType stoneType) {
    ImmutableSet.Builder<Threat> builder = ImmutableSet.builder();
    for (int i = 0; i < 5; i++) {
      for (int j = i + 1; j < 5; j++) {
        MoveType[] moves = new MoveType[] {X, X, X, X, X};
        moves[i] = D;
        moves[j] = O;
        builder.addAll(createThreats(stoneType, moves));
        moves[i] = O;
        moves[j] = D;
        builder.addAll(createThreats(stoneType, moves));
      }
    }
    return builder.build();
  }

  private ImmutableSet<Threat> createThreeWithThreats(StoneType stoneType) {
    ImmutableSet.Builder<Threat> builder = ImmutableSet.builder();
    for (int i = 1; i < 5; i++) {
      for (int j = i + 1; j < 5; j++) {
        MoveType[] moves = new MoveType[] {D, X, X, X, X, D};
        moves[i] = D;
        moves[j] = O;
        builder.addAll(createThreats(stoneType, moves));

        moves[i] = O;
        moves[j] = X;
        builder.addAll(createThreats(stoneType, moves));
      }
    }
    for (int i = 2; i < 5; i++) {
      MoveType[] moves = new MoveType[] {E, D, X, X, X, D, E};
      moves[i] = O;
      builder.addAll(createThreats(stoneType, moves));
    }
    return builder.build();
  }

  // TODO move this into Util class.
  private Iterable<Threat> createThreats(
      StoneType stoneType,
      MoveType[] movePattern) {
    int originalPattern = generatePattern(movePattern, stoneType.getBits());
    int patternLength = movePattern.length;
    List<Threat> result = new ArrayList<>();
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      int mask = (1 << (2 * patternLength)) - 1;
      int pattern = originalPattern;
      for (int j = 0; j <= Constants.BOARD_SIZE - patternLength; j++) {
        result.add(createThreat(i, j, pattern, mask, PositionTransformer.IDENTITY,
            stoneType, movePattern));
        result.add(createThreat(i, j, pattern, mask, PositionTransformer.CLOCK_90,
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
        result.add(createThreat(i, j, pattern, mask, PositionTransformer.RIGHT_DIAGONAL,
            stoneType, movePattern));
        result.add(createThreat(i, j, pattern, mask, PositionTransformer.LEFT_DIAGONAL,
            stoneType, movePattern));
        mask <<= 2;
        pattern <<= 2;
      }
    }
    return result;
  }

  public ImmutableSet<Threat> get(StoneType stoneType, PatternType PatternType) {
    return threats.get(stoneType).get(PatternType);
  }

  public Set<Threat> get(int i, int j, StoneType stoneType) {
    return index.get(stoneType).get(Position.create(i, j));
  }

  private Threat createThreat(int i, int j, int pattern, int mask,
                      PositionTransformer transformer, StoneType stoneType,
                      MoveType[] movePattern) {
    Threat result = new ThreatImpl(i, pattern, mask, transformer, stoneType,
        getDefensiveMoves(i, j, movePattern, transformer.reverse()),
        getOffensiveMove(i, j, movePattern, transformer.reverse()));
    PositionTransformer reverseTransform = transformer.reverse();
    for (int k = 0; k < movePattern.length; k++) {
      if (movePattern[k] == MoveType.X) {
        Position p = Position.create(i, j + k).transform(reverseTransform);
        index.get(stoneType).get(p).add(result);
      }
    }
    return result;
  }
}
