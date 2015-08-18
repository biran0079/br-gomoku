package ai.threatbasedsearch;

import static org.junit.Assert.assertTrue;

import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardFactories;
import common.boardclass.testing.BoardClassUtil;
import common.pattern.Threat;

import org.junit.Test;

/**
 * Unit test for ThreatBasedSearch.
 */
public class ThreatBasedSearchTest {

  private final  BoardClass.Factory<Threat> factory =
      BoardFactories.BOARD_CLASS_WITH_MATCHING_THREATS_FACTORY;
  private final ThreatBasedSearch threatBasedSearch = new ThreatBasedSearch();

  @Test
  public void testWinningPathExists() throws Exception {
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

    assertTrue(threatBasedSearch.winningPathExists(boardClass, StoneType.BLACK));
  }

  private BoardClass<Threat> createBoard(String s) {
    return factory.fromGameBoard(BoardClassUtil.fromString(s));
  }
}