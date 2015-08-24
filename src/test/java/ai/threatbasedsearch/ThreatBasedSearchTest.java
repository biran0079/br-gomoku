package ai.threatbasedsearch;

import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardFactories;
import common.boardclass.testing.BoardClassUtil;
import common.pattern.Threat;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for ThreatBasedSearch.
 */
public class ThreatBasedSearchTest {

  private final  BoardClass.Factory<Threat> factory =
      BoardFactories.FOR_THREAT;
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

    assertNotNull(threatBasedSearch.winningMove(boardClass, StoneType.BLACK));
  }

  private BoardClass<Threat> createBoard(String s) {
    return factory.fromGameBoard(BoardClassUtil.fromString(s));
  }
}