package ai.threatbasedsearch;

import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardFactories;
import common.boardclass.testing.BoardClassUtil;
import common.pattern.Threat;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by biran on 9/13/2015.
 */
public class DatabaseManagerTest {

  private final  BoardClass.Factory<Threat> factory =
      BoardFactories.FOR_THREAT;
  private final ThreatBasedSearch threatBasedSearch = new ThreatBasedSearch();
  private DatabaseManager databaseManager;

  @Before
  public void setUp() throws SQLException {
    databaseManager = DatabaseManager.inMemoryDb();
  }

  @Test
  public void testSaveTree() throws SQLException {
    BoardClass<Threat> boardClass =
         factory.fromGameBoard(BoardClassUtil.fromString(
             "_______________\n"
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
            + "_______________\n"));
    long id = databaseManager.saveTreeIfNotExist(
        threatBasedSearch.threatBasedTree(boardClass, StoneType.BLACK));
    System.err.println(databaseManager.loadTree(id));
  }
}