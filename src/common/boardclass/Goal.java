package common.boardclass;

import com.google.common.collect.ImmutableSet;
import common.PositionTransformer;
import common.StoneType;
import common.pattern.AbstractPattern;
import common.pattern.MoveType;
import model.Position;

import java.util.EnumMap;
import java.util.Map;

import static common.pattern.MoveType.X;
import static common.pattern.PatternsUtil.createPatterns;

/**
 * Goal patterns that indicates termination of game.
 */
class Goal extends AbstractPattern {

  static Map<StoneType, ImmutableSet<AbstractPattern>> GOALS = initializeGoals();

  private Goal(int row, int col, int pattern, int mask,
       PositionTransformer transformer,
       StoneType stoneType,
       MoveType[] movePattern) {
    super(row, pattern, mask, transformer, stoneType, ImmutableSet.<Position>of());
  }

  private static Map<StoneType, ImmutableSet<AbstractPattern>> initializeGoals() {
    Map<StoneType, ImmutableSet<AbstractPattern>> result = new EnumMap<>(StoneType.class);
    result.put(StoneType.BLACK, createGoalPatterns(StoneType.BLACK));
    result.put(StoneType.WHITE, createGoalPatterns(StoneType.WHITE));
    return result;
  }

  private static ImmutableSet<AbstractPattern> createGoalPatterns(StoneType stoneType) {
    return ImmutableSet.<AbstractPattern>builder()
        .addAll(createPatterns(stoneType, new MoveType[]{X, X, X, X, X}, Goal::new))
        .build();
  }
}
