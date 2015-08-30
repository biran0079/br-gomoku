package common.pattern;

import com.google.common.collect.ImmutableSet;
import common.Constants;
import common.PositionTransformer;
import common.StoneType;
import model.Position;

import java.util.*;

/**
 * Utility methods for patterns.
 */
public class PatternsUtil {

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

  public static ImmutableSet<Position> getDefensiveMoves(int i, int j,
                                                         MoveType[] movePattern,
                                                         PositionTransformer transformer) {
    ImmutableSet.Builder<Position> builder = ImmutableSet.builder();
    for (int k = 0; k < movePattern.length; k++) {
      if (movePattern[k] == MoveType.D) {
        builder.add(Position.of(i, j + k).transform(transformer));
      }
    }
    return builder.build();
  }

  public static Position getOffensiveMove(int i, int j, MoveType[] movePattern, PositionTransformer transformer) {
    for (int k = 0; k < movePattern.length; k++) {
      if (movePattern[k] == MoveType.O) {
        return Position.of(i, j + k).transform(transformer);
      }
    }
    return null;
  }

  public static ImmutableSet<Position> getDependingMoves(int i, int j,
                                                          MoveType[] movePattern,
                                                          PositionTransformer transformer) {
    ImmutableSet.Builder<Position> builder = ImmutableSet.builder();
    for (int k = 0; k < movePattern.length; k++) {
      if (movePattern[k] == MoveType.X) {
        builder.add(Position.of(i, j + k).transform(transformer));
      }
    }
    return builder.build();
  }

  public static <T> Map<StoneType, Map<Position, Set<T>>> emptyIndex() {
    Map<StoneType, Map<Position, Set<T>>> result = new EnumMap<>(StoneType.class);
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE}) {
      Map<Position, Set<T>> innerMap = new HashMap<>();
      result.put(stoneType, innerMap);
      for (int i = 0; i < Constants.BOARD_SIZE; i++) {
        for (int j = 0; j < Constants.BOARD_SIZE; j++) {
          innerMap.put(Position.of(i, j), new HashSet<>());
        }
      }
    }
    return result;
  }

  public static <T extends Pattern> Iterable<T> createPatterns(
      StoneType stoneType,
      MoveType[] movePattern,
      PatternType patternType,
      Pattern.Factory<T> factory) {
    int originalPattern = PatternsUtil.generatePattern(movePattern, stoneType.getBits());
    int patternLength = movePattern.length;
    List<T> result = new ArrayList<>();
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      int mask = (1 << (2 * patternLength)) - 1;
      int pattern = originalPattern;
      for (int j = 0; j <= Constants.BOARD_SIZE - patternLength; j++) {
        result.add(factory.create(i, j, pattern, mask, PositionTransformer.IDENTITY,
            stoneType, movePattern, patternType));
        result.add(factory.create(i, j, pattern, mask, PositionTransformer.CLOCK_90,
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
        result.add(factory.create(i, j, pattern, mask, PositionTransformer.RIGHT_DIAGONAL,
            stoneType, movePattern, patternType));
        result.add(factory.create(i, j, pattern, mask, PositionTransformer.LEFT_DIAGONAL,
            stoneType, movePattern, patternType));
        mask <<= 2;
        pattern <<= 2;
      }
    }
    return result;
  }
}
