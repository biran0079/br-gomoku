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
public class PatternsWithIndex implements Pattern.Factory {

  private final Map<StoneType, Map<Position, Set<Pattern>>> index;
  private final Patterns patterns;

  PatternsWithIndex() {
    index = emptyIndex();
    patterns = new Patterns() {
      @Override
      Pattern createPattern(int i, int j, int pattern, int mask,
                            PositionTransformer transformer, StoneType stoneType, MoveType[] movePattern) {
        Pattern result = super.createPattern(i, j, pattern, mask, transformer, stoneType, movePattern);
        PositionTransformer reverseTransform = transformer.reverse();
        for (int k = 0; k < movePattern.length; k++) {
          StoneType typeAtK = movePattern[k] == MoveType.X ? stoneType : StoneType.NOTHING;
          Position p = Position.create(i, j + k).transform(reverseTransform);
          index.get(typeAtK).get(p).add(result);
        }
        return result;
      }
    };
  }


  public Set<Pattern> get(int i, int j, StoneType stoneType) {
    return index.get(stoneType).get(Position.create(i, j));
  }

  private Map<StoneType, Map<Position, Set<Pattern>>> emptyIndex() {
    Map<StoneType, Map<Position, Set<Pattern>>> result = new EnumMap<>(StoneType.class);
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE, StoneType.NOTHING}) {
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

  @Override
  public ImmutableSet<Pattern> get(StoneType stoneType, PatternType patternType) {
    return patterns.get(stoneType, patternType);
  }
}
