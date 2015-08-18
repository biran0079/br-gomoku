package ai.competition;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import common.StoneType;
import common.boardclass.BoardClass;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import ai.AI;
import model.Position;

/**
 * Competition between AIs by playing games against each other.
 */
public class Competition {

  private final Collection<BoardClass<?>> boardClasses;

  public Competition(Collection<BoardClass<?>> boardClasses) {
    this.boardClasses = boardClasses;
  }

  public void competeSequential(AI ai1, AI ai2, StoneType firstMove) {
    compete(ai1, ai2, firstMove, false);
  }

  public void competeParallel(AI ai1, AI ai2, StoneType firstMove) {
    compete(ai1, ai2, firstMove, true);
  }

  private void compete(AI ai1, AI ai2, StoneType firstMove, boolean runInParallel) {
    StoneType secondMove = firstMove.getOpponent();
    CompetitionAI competitionAI1 = new CompetitionAI(ai1);
    CompetitionAI competitionAI2 = new CompetitionAI(ai2);

    Stopwatch stopwatch = Stopwatch.createStarted();
    Stream<BoardClass<?>> gameStream = boardClasses.stream();
    if (runInParallel) {
      gameStream = gameStream.parallel();
    }
    gameStream
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
      BoardClass<?> boardClass,
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
