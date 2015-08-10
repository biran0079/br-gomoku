package ai.competition;

import ai.AI;
import ai.minmax.MinMaxSearch;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterables;
import com.google.common.math.DoubleMath;
import common.BoardClass;
import common.Patterns;
import common.StoneType;
import model.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Competition between AIs by playing games against each other.
 */
public class Competition {

  private final Collection<BoardClass> boardClasses;

  Competition(Collection<BoardClass> boardClasses) {
    this.boardClasses = boardClasses;
  }

  void compete(AI ai1, AI ai2, StoneType firstMove) {
    AIWithStats ai1WithStats = new AIWithStats(ai1);
    AIWithStats ai2WithStats = new AIWithStats(ai2);
    for (BoardClass gameBoard : boardClasses) {
      StoneType secondMove = firstMove.getOpponent();
      competeSingleGameWithMoveOrder(gameBoard,
          new AIWithStats[] {ai1WithStats, ai2WithStats},
          new StoneType[] {firstMove, secondMove});
      competeSingleGameWithMoveOrder(gameBoard,
          new AIWithStats[] {ai2WithStats, ai1WithStats},
          new StoneType[] {firstMove, secondMove});
      System.err.println(ai1WithStats);
      System.err.println(ai2WithStats);
    }
  }

  private void competeSingleGameWithMoveOrder(BoardClass gameBoard, AIWithStats[] ai, StoneType[] stoneType) {
    BoardClass boardClass = BoardClass.fromGameBoard(gameBoard);
    int i = 0;
    while (true) {
      if (boardClass.matchesAny(Patterns.getGoalPatterns(stoneType[0]))) {
        ai[0].win++;
        ai[1].loss++;
        break;
      }
      if (boardClass.matchesAny(Patterns.getGoalPatterns(stoneType[1]))) {
        ai[1].win++;
        ai[0].loss++;
        break;
      }
      if (boardClass.isFull()) {
        ai[1].draw++;
        ai[0].draw++;
        break;
      }
      Stopwatch stopwatch = Stopwatch.createStarted();
      Position move = ai[i].ai.nextMove(boardClass, stoneType[i]);
      ai[i].durationMsPerMove.add(stopwatch.elapsed(TimeUnit.MILLISECONDS));
      boardClass = boardClass.withPositionSet(move.getRowIndex(), move.getColumnIndex(), stoneType[i]);
      i = 1 - i;
    }
  }

  private static class AIWithStats {
    private final AI ai;
    private final List<Long> durationMsPerMove = new ArrayList<>();
    private int win, loss, draw;

    AIWithStats(AI ai) {
      this.ai = ai;
    }

    @Override
    public String toString() {
      return new StringBuilder()
          .append(ai)
          .append( " ")
          .append(win)
          .append(" wins, ")
          .append(loss)
          .append(" losses, ")
          .append(draw)
          .append(" draw, ")
          .append("average move duration: ")
          .append(String.format("%.2f", DoubleMath.mean(durationMsPerMove)))
          .append(" ms.")
          .toString();

    }
  }

  public static void main(String[] args) {
    AI ai1 = MinMaxSearch.newBuilder()
        .setName("new-eval")
        .setMaxDepth(3)
        .setEvaluator(
            (boardClass, blackMoveNext) -> {
              if (blackMoveNext) {
                if (boardClass.matchesAny(Patterns.BLACK_STRAIT_FOUR)
                    || boardClass.matchesAny(Patterns.BLACK_THREE)) {
                  return 10;
                }
                return 0;
              } else {
                if (boardClass.matchesAny(Patterns.WHITE_STRAIT_FOUR)
                    || boardClass.matchesAny(Patterns.WHITE_THREE)) {
                  return -10;
                }
                return 0;
              }
            })
        .build();
    AI ai2 = MinMaxSearch.newBuilder()
        .setName("d_3")
        .setMaxDepth(3)
        .build();
    new Competition(BoardClassSamples.INITIAL_10).compete(ai1, ai2, StoneType.BLACK);
  }
}
