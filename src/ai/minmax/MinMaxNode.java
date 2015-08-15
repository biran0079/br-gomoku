package ai.minmax;

import common.PositionTransformer;
import common.Transformable;

import model.Position;

import static common.PositionTransformer.*;
import static common.PositionTransformer.CLOCK_270_M;

/**
 * Node of min-max search tree.
 */
public class MinMaxNode implements Transformable<MinMaxNode> {

  private final Position bestMove;
  private final int score;

  MinMaxNode(Position bestMove, int score) {
    this.bestMove = bestMove;
    this.score = score;
  }

  public Position getBestMove() {
    return bestMove;
  }

  public int getScore() {
    return score;
  }

  @Override
  public MinMaxNode transform(PositionTransformer transformer) {
    if (bestMove == null || transformer == IDENTITY) {
      return this;
    }
    return new MinMaxNode(bestMove.transform(transformer), score);
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("position: ")
        .append(bestMove)
        .append(", store: ")
        .append(score)
        .toString();
  }
}
