package ai.minmax;

import static org.junit.Assert.assertTrue;

import common.BoardClass;
import common.BoardClassUtil;
import common.Patterns;

import org.junit.Test;

/**
 * Some unit tests for BoardClass.
 */
public class BoardClassTest {

  @Test
  public void testSample() {
    BoardClass boardClass = BoardClassUtil.fromString(
        "_______________\n" +
            "_______________\n" +
            "_______________\n" +
            "_______________\n" +
            "_______________\n" +
            "_______________\n" +
            "_______________\n" +
            "_______________\n" +
            "_______________\n" +
            "__________O____\n" +
            "_________O_____\n" +
            "________O______\n" +
            "_______O_______\n" +
            "_______________\n" +
            "_______________");
    assertTrue(boardClass.matchesAny(Patterns.BLACK_STRAIT_FOUR));
  }
}