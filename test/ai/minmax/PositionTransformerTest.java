package ai.minmax;

import static org.junit.Assert.assertEquals;

import common.Constants;
import common.PositionTransformer;

import org.junit.Test;

import model.Position;

/**
 * Created by biran on 8/9/2015.
 */
public class PositionTransformerTest {

  @Test
  public void testReverse() {
    for (int i = 0; i < Constants.BOARD_SIZE; i++)
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        assertEquals(Position.of(i, j),
            Position.of(i, j)
                .transform(PositionTransformer.RIGHT_DIAGONAL)
                .transform(PositionTransformer.RIGHT_DIAGONAL_REVERSE));
        assertEquals(Position.of(i, j),
            Position.of(i, j)
                .transform(PositionTransformer.LEFT_DIAGONAL)
                .transform(PositionTransformer.LEFT_DIAGONAL_REVERSE));
      }
  }
}