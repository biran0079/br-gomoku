package ai.competition;

import ai.AI;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import common.BoardClass;
import common.Patterns;
import common.StoneType;
import model.Position;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Competition between AIs by playing games against each other.
 */
public class Competition {

  private final Collection<BoardClass> boardClasses;

  public Competition(Collection<BoardClass> boardClasses) {
    this.boardClasses = boardClasses;
  }

  public Competition(BoardClass boardClasses) {
    this(Collections.singleton(boardClasses));
  }

  public void compete(AI ai1, AI ai2, StoneType firstMove) {

    StoneType secondMove = firstMove.getOpponent();
    CompetitionAI competitionAI1 = new CompetitionAI(ai1);
    CompetitionAI competitionAI2 = new CompetitionAI(ai2);

    Stopwatch stopwatch = Stopwatch.createStarted();
    boardClasses.stream()
        .parallel()
        .flatMap(boardClass ->
            Lists.<Runnable>newArrayList(
                () -> competeSingleGameWithMoveOrder(boardClass,
                    new CompetitionAI[]{competitionAI1, competitionAI2},
                    new StoneType[]{firstMove, secondMove}),
                () -> competeSingleGameWithMoveOrder(boardClass,
                    new CompetitionAI[]{competitionAI2, competitionAI1},
                    new StoneType[]{firstMove, secondMove})
            ).stream())
        .forEach(runnable -> {
          runnable.run();
          System.out.println(competitionAI1);
          System.out.println(competitionAI2);
        });
    System.out.printf("Total duration for %s vs %s: %.2f src.\n",
        ai1,
        ai2,
        stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0);
  }

  private void competeSingleGameWithMoveOrder(
      BoardClass boardClass,
      CompetitionAI[] ai,
      StoneType[] stoneType) {
    int i = 0;
    while (true) {
      if (boardClass.wins(stoneType[0])) {
        ai[0].incWin();
        ai[1].incLose();
        break;
      }
      if (boardClass.wins(stoneType[1])) {
        ai[1].incWin();
        ai[0].incLose();
        break;
      }
      if (boardClass.isFull()) {
        ai[1].incDraw();
        ai[0].incDraw();
        break;
      }
      Stopwatch stopwatch = Stopwatch.createStarted();
      Position move = ai[i].getAI().nextMove(boardClass, stoneType[i]);
      ai[i].recordMoveTimeCost(stopwatch.elapsed(TimeUnit.MILLISECONDS));
      boardClass = boardClass.withPositionSet(move.getRowIndex(), move.getColumnIndex(), stoneType[i]);
      i = 1 - i;
    }
  }
}
