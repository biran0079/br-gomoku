package ai.threatbasedsearch;

import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardFactories;
import common.boardclass.testing.BoardClassUtil;
import common.pattern.Threat;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for CompleteThreatSearch.
 */
public class AggresiveThreatSearchTest {

  private final  BoardClass.Factory<Threat> factory =
      BoardFactories.FOR_THREAT;
  private final AggresiveThreatSearch aggresiveThreatSearch = new AggresiveThreatSearch();

  @Test
  public void testWinningMove() throws Exception {
    BoardClass<Threat> boardClass = createBoard(""
        + "_______________\n"
        + "_______________\n"
        + "_______________\n"
        + "_______________\n"
        + "_______________\n"
        + "______O________\n"
        + "______X_XO_____\n"
        + "______XO__X____\n"
        + "______X_OO_____\n"
        + "_______________\n"
        + "_______________\n"
        + "_______________\n"
        + "_______________\n"
        + "_______________\n"
        + "_______________\n");

    assertNotNull(aggresiveThreatSearch.winningMove(boardClass, StoneType.BLACK, 6));
  }

  @Test
  public void testWinningMove2() {
    BoardClass<Threat> boardClass = createBoard("" +
        "_______________\n" +
        "_______________\n" +
        "_______________\n" +
        "_______________\n" +
        "_______________\n" +
        "__________X____\n" +
        "______XXOXX____\n" +
        "_______OXOO____\n" +
        "______XOOOX____\n" +
        "_______O_______\n" +
        "_______________\n" +
        "_______________\n" +
        "_______________\n" +
        "_______________\n" +
        "_______________");
    assertNotNull(aggresiveThreatSearch.winningMove(boardClass, StoneType.BLACK, 4));
  }

  private BoardClass<Threat> createBoard(String s) {
    return factory.fromGameBoard(BoardClassUtil.fromString(s));
  }
}