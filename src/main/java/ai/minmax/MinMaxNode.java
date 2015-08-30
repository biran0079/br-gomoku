package ai.minmax;

import com.google.auto.value.AutoValue;
import com.sun.istack.internal.Nullable;
import common.PositionTransformer;
import common.Transformable;
import model.Position;

import static common.PositionTransformer.IDENTITY;

/**
 * Node of min-max search tree.
 */
@AutoValue
public abstract class MinMaxNode implements Transformable<MinMaxNode> {

  public static MinMaxNode create(Position bestMove, int score) {
    return new AutoValue_MinMaxNode(bestMove, score);
  }

  @Nullable public abstract Position getBestMove();

  public abstract int getScore();

  @Override
  public MinMaxNode transform(PositionTransformer transformer) {
    if (getBestMove() == null || transformer == IDENTITY) {
      return this;
    }
    return create(getBestMove().transform(transformer), getScore());
  }
}
