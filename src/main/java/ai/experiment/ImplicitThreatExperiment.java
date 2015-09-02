package ai.experiment;

import ai.threatbasedsearch.ThreatBasedSearch;
import com.google.common.base.Stopwatch;
import common.Constants;
import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardFactories;
import common.boardclass.testing.BoardClassUtil;
import common.pattern.Threat;
import model.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

/**
 * Created by biran on 9/1/2015.
 */
public class ImplicitThreatExperiment {

  private static final ThreatBasedSearch threatBasedSearch = new ThreatBasedSearch();

  private static final BoardClass<Threat> boardClass =
      BoardFactories.FOR_THREAT.fromGameBoard(
          BoardClassUtil.fromString("_______________\n"
              + "_______________\n"
              + "_______________\n"
              + "_______________\n"
              + "_______________\n"
              + "______O________\n"
              + "______X_XO_____\n"
              + "______XO_______\n"
              + "______X_OO_____\n"
              + "_______________\n"
              + "_______________\n"
              + "_______________\n"
              + "_______________\n"
              + "_______________\n"
              + "_______________\n"));


  public static void main(String[] args) {
    Stopwatch st = Stopwatch.createStarted();
    try{
      naive();
    } finally {
      System.err.println(st.elapsed(TimeUnit.MILLISECONDS));
    }
    st = Stopwatch.createStarted();
    try{
      fancy();
    } finally {
      System.err.println(st.elapsed(TimeUnit.MILLISECONDS));
    }
  }

  private static void naive() {
    Set<Position> implicitThreats = new HashSet<>();
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        Position p = Position.of(i, j);
        if (boardClass.get(p) == StoneType.NOTHING
            && threatBasedSearch.winningMove(
            boardClass.withPositionSet(p, StoneType.WHITE),
            StoneType.BLACK) == null) {
          implicitThreats.add(p);
        }
      }
    }
    printBoardWithImplicitThreats(implicitThreats);
  }

  private static void fancy() {
    printBoardWithImplicitThreats(threatBasedSearch.findImplicitThreats(boardClass, StoneType.BLACK));
  }

  private static void printBoardWithImplicitThreats(Set<Position> implicitThreats) {
    String[] s = boardClass.toString().split("\n");
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        Position p = Position.of(i, j);
        if (implicitThreats.contains(p)) {
          System.err.print("\uFF29");
        } else {
          System.err.print(s[i].charAt(j));
        }
      }
      System.err.println();
    }
  }
}
