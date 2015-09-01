package ai.threatbasedsearch;

import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardFactories;
import common.boardclass.testing.BoardClassUtil;
import common.pattern.Threat;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Unit test for ThreatBasedSearch.
 */
public class ThreatBasedSearchTest {

  private final  BoardClass.Factory<Threat> factory =
      BoardFactories.FOR_THREAT;
  private final ThreatBasedSearch threatBasedSearch = new ThreatBasedSearch();

  @Test
  public void testWinningPathExists() throws Exception {
    BoardClass<Threat> boardClass = createBoard("_______________\n" +
            "_______________\n" +
            "_______________\n" +
            "______O________\n" +
            "_______X_______\n" +
            "_______OXXX____\n" +
            "_______OOXO____\n" +
            "_____XOOOOX____\n" +
            "_______OXXXO___\n" +
            "_______XXOX____\n" +
            "_______O__O____\n" +
            "_______________\n" +
            "_______________\n" +
            "_______________\n" +
            "_______________\n");

    assertNotNull(threatBasedSearch.winningMove(boardClass, StoneType.WHITE));
  }

  @Test
  public void testWinningPathExists2() throws Exception {
    BoardClass<Threat> boardClass = createBoard( "_______________\n"
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

  @Test
  public void testWinningPathExists3() throws Exception {
    BoardClass<Threat> boardClass = createBoard("" +
        "X______________\n" +
        "_X_____________\n" +
        "__X_________O__\n" +
        "____________O__\n" +
        "_______________\n" +
        "_____________X_\n" +
        "______OO_X_____\n" +
        "______OXX_O____\n" +
        "______X_O__OX__\n" +
        "________O______\n" +
        "__X____X_X_____\n" +
        "_X_____________\n" +
        "X______________\n" +
        "_______________\n" +
        "_______________");

    assertNotNull(threatBasedSearch.winningMove(boardClass, StoneType.BLACK));
  }


  @Test
  public void testWinningPathExists4() throws Exception {
    BoardClass<Threat> boardClass = createBoard("©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à¡ñ©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à¡ð©à¡ð¡ñ©à©à©à©à©à©à©à\n" +
        "©à©à©à©à¡ð¡ñ©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à¡ð©à¡ñ¡ñ©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à");
    assertNotNull(threatBasedSearch.winningMove(boardClass, StoneType.BLACK));
  }

  @Test
  public void testWinningPathExists5() throws Exception {
    BoardClass<Threat> boardClass = createBoard("©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à¡ð¡ð¡ð¡ñ©à©à©à©à\n" +
        "©à©à©à©à©à©à¡ð¡ñ¡ñ¡ñ©à©à©à©à©à\n" +
        "©à©à©à©à©à©à¡ð©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à¡ð¡ñ¡ñ¡ñ©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à¡ð¡ð¡ð¡ñ©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à");
    assertNotNull(threatBasedSearch.winningMove(boardClass, StoneType.BLACK));
  }

  @Test
  public void testWinningPathExists6() throws Exception {
    BoardClass<Threat> boardClass = createBoard("©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à¡ð©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à¡ñ¡ñ¡ñ¡ð©à©à\n" +
        "©à©à©à©à¡ð¡ñ¡ñ¡ñ©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à¡ð¡ñ¡ñ¡ñ©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à¡ñ¡ñ¡ñ¡ð©à©à\n" +
        "©à©à©à©à©à©à©à©à¡ð©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à");
    assertNotNull(threatBasedSearch.winningMove(boardClass, StoneType.BLACK));
  }

  @Test
  public void testWinningPathExists7() throws Exception {
    BoardClass<Threat> boardClass = createBoard("©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à¡ð¡ð¡ð©à¡ñ¡ñ¡ñ©à\n" +
        "©à©à©à©à©à©à¡ð¡ñ¡ñ¡ñ©à©à©à©à©à\n" +
        "©à©à©à©à©à©à¡ð©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à¡ð¡ñ¡ñ¡ñ©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à¡ð¡ð¡ð©à¡ñ¡ñ©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n" +
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à");
    assertNotNull(threatBasedSearch.winningMove(boardClass, StoneType.BLACK));
  }

  @Test
  public void testWinningPathExists8() throws Exception {
    BoardClass<Threat> boardClass = createBoard("©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
            "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
            "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
            "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
            "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
            "©à©à©à©à©à©à©à©à©à¡ñ¡ñ©à©à©à©à\n"+
            "©à©à©à©à¡ð¡ñ¡ñ¡ñ©à©à©à©à©à©à©à\n"+
            "©à©à©à©à¡ð©à©à©à©à©à©à©à©à©à©à\n"+
            "©à©à©à©à¡ð¡ñ¡ñ¡ñ©à©à©à©à©à©à©à\n"+
            "©à©à©à©à©à©à©à©à©à¡ñ¡ñ©à©à©à©à\n"+
            "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
            "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
            "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
            "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
            "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à");
    assertNotNull(threatBasedSearch.winningMove(boardClass, StoneType.BLACK));
  }

  @Test
  public void testWinningPathExists9() throws Exception {
    BoardClass<Threat> boardClass = createBoard("©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
        "©à©à©à©à©à©à©à¡ñ©à©à©à©à©à©à©à\n"+
        "©à©à©à©à©à©à¡ñ©à¡ñ©à©à©à©à©à©à\n"+
        "©à©à©à©à©à©à©à¡ñ©à©à©à©à©à©à©à\n"+
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
        "©à©à©à©à©à©à©à©à©à©à©à©à©à©à©à\n"+
        "©à©à¡ñ¡ð¡ð¡ð©à©à©à¡ð¡ð¡ð¡ñ©à©à");
    assertNull(threatBasedSearch.winningMove(boardClass, StoneType.BLACK));
  }



  private BoardClass<Threat> createBoard(String s) {
    return factory.fromGameBoard(BoardClassUtil.fromString(s));
  }
}