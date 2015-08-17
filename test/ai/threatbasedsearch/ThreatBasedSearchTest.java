package ai.threatbasedsearch;

import common.StoneType;
import common.boardclass.BoardClassWithMatchingThreats;
import common.boardclass.testing.BoardClassUtil;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for ThreatBasedSearch.
 */
public class ThreatBasedSearchTest {

  private final BoardClassWithMatchingThreats.Factory factory =
      new BoardClassWithMatchingThreats.Factory();
  private final ThreatBasedSearch threatBasedSearch = new ThreatBasedSearch();

  @Test
  public void testWinningPathExists() throws Exception {
    BoardClassWithMatchingThreats boardClass = createBoard(""
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

  private BoardClassWithMatchingThreats createBoard(String s) {
    return factory.fromGameBoard(BoardClassUtil.fromString(s));
  }
}