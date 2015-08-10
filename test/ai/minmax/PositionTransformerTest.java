package ai.minmax;

import common.PositionTransformer;
import common.Constants;
import model.Position;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by biran on 8/9/2015.
 */
public class PositionTransformerTest {

  @Test
  public void testReverse() {
    for (int i = 0; i < Constants.BOARD_SIZE; i++)
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        assertEquals(Position.create(i, j),
            Position.create(i, j)
                .transform(PositionTransformer.RIGHT_DIAGONAL)
                .transform(PositionTransformer.RIGHT_DIAGONAL_REVERSE));
        assertEquals(Position.create(i, j),
            Position.create(i, j)
                .transform(PositionTransformer.LEFT_DIAGONAL)
                .transform(PositionTransformer.LEFT_DIAGONAL_REVERSE));
      }
  }
}