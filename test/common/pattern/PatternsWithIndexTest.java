package common.pattern;

import common.Constants;
import common.StoneType;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PatternsWithIndexTest {

  private PatternsWithIndex patternsWithIndex = new PatternsWithIndex();

  @Test
  public void testGet() {
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        for (StoneType stoneType : StoneType.values()) {
          for (Pattern pattern : patternsWithIndex.get(i, j, stoneType)) {
            assertEquals(stoneType.toString().charAt(0),
                pattern.toString().split("\n")[i].charAt(j));
          }
        }
      }
    }
  }
}