package ai.minmax;

import common.*;
import common.boardclass.BoardClass;
import common.boardclass.BoardClassUtil;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Some unit tests for BoardClass.
 */
public class BoardClassImplTest {

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
    assertTrue(boardClass.matchesAny(StoneType.BLACK, PatternType.STRAIT_FOUR));
    assertTrue(boardClass.matchesAny(StoneType.BLACK, PatternType.OPEN_FOUR));
  }
}