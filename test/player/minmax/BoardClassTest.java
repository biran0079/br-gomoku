package player.minmax;

import com.google.common.collect.Lists;
import common.Square;
import model.GameBoard;
import model.Position;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static player.minmax.BoardClassUtil.parseGameBoard;

/**
 * Some unit tests for BoardClass.
 */
public class BoardClassTest {

  @Test
  public void testSample() {
    BoardClass boardClass = BoardClass.fromGameBoard(
        parseGameBoard(
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
            "_______________"));
    assertTrue(boardClass.matchesAny(Patterns.BLACK_STRAIT_FOUR));
  }
}