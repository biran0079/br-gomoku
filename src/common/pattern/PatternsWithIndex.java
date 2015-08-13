package common.pattern;

import com.google.common.collect.ImmutableSet;

import common.Constants;
import common.PatternType;
import common.PositionTransformer;
import common.StoneType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.Position;

/**
 * Pattern factory that index patterns by position and stone type.
 */
public class PatternsWithIndex extends Patterns {

  private final Map<StoneType, Map<Position, Set<Pattern>>> index = emptyIndex();

  @Override
  Pattern createPattern(int i, int j, int pattern, int mask,
      PositionTransformer transformer, StoneType stoneType, MoveType[] movePattern) {
    Pattern result = super.createPattern(i, j, pattern, mask, transformer, stoneType, movePattern);
    for (int k = 0; k < movePattern.length; k++) {
      Position p = Position.create(i, j + k).transform(transformer.reverse());
      index.get(pattern).get(p).add(result);
    }
    return result;
  }

  public Set<Pattern> get(int i, int j, StoneType stoneType) {
    return index.get(stoneType).get(Position.create(i, j));
  }

  private Map<StoneType, Map<Position, Set<Pattern>>> emptyIndex() {
    Map<StoneType, Map<Position, Set<Pattern>>> result = new EnumMap<>(StoneType.class);
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE}) {
      Map<Position, Set<Pattern>> innerMap = new HashMap<>();
      result.put(stoneType, innerMap);
      for (int i = 0; i < Constants.BOARD_SIZE; i++) {
        for (int j = 0; j < Constants.BOARD_SIZE; j++) {
          innerMap.put(Position.create(i, j), new HashSet<>());
        }
      }
    }
    return result;
  }
}
