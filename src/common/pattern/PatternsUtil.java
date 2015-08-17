package common.pattern;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import common.Constants;
import common.PositionTransformer;
import common.StoneType;
import model.Position;

import java.util.*;

import static common.pattern.MoveType.O;

/**
 * Utility methods for patterns.
 */
class PatternsUtil {

  static int generatePattern(MoveType[] moves, int bits) {
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

  static ImmutableSet<Position> getDefensiveMoves(int i,
                                                  int j,
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

  static Position getOffensiveMove(int i, int j, MoveType[] movePattern, PositionTransformer transformer) {
    for (int k = 0; k < movePattern.length; k++) {
      if (movePattern[k] == MoveType.O) {
        return Position.of(i, j + k).transform(transformer);
      }
    }
    throw new IllegalStateException("No offensive move found");
  }

  static <T> Map<StoneType, Map<Position, Set<T>>> emptyIndex() {
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
}
