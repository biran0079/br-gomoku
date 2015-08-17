package common.pattern;

import com.google.common.collect.ImmutableSet;

import common.PatternType;
import common.PositionTransformer;
import common.StoneType;

import java.util.Map;
import java.util.Set;

import model.Position;

/**
 * Pattern factory that index patterns by position and stone type.
 */
public class PatternsWithIndex implements Pattern.Factory {

  private final Map<StoneType, Map<Position, Set<Pattern>>> index;
  private final Patterns patterns;

  public PatternsWithIndex() {
    index = PatternsUtil.emptyIndex();
    patterns = new Patterns() {
      @Override
      Pattern createPattern(int i, int j, int pattern, int mask,
                            PositionTransformer transformer, StoneType stoneType,
                            MoveType[] movePattern) {
        Pattern result = super.createPattern(i, j, pattern, mask, transformer,
            stoneType, movePattern);
        PositionTransformer reverseTransform = transformer.reverse();
        for (int k = 0; k < movePattern.length; k++) {
          if (movePattern[k] == MoveType.X) {
            Position p = Position.create(i, j + k).transform(reverseTransform);
            index.get(stoneType).get(p).add(result);
          }
        }
        return result;
      }
    };
  }

  public Set<Pattern> get(int i, int j, StoneType stoneType) {
    return index.get(stoneType).get(Position.create(i, j));
  }

  @Override
  public ImmutableSet<Pattern> get(StoneType stoneType, PatternType patternType) {
    return patterns.get(stoneType, patternType);
  }
}
