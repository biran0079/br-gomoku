package ai.competition;

import ai.AI;
import com.google.common.math.DoubleMath;

import java.util.Collections;
import java.util.Vector;
import java.util.stream.LongStream;

/**
 * Holder of competition AI and competition results for the AI.
 */
public class CompetitionAI {

  private volatile int win, lose, draw;
  private final Vector<Long> moveTimeCostMs = new Vector<>();
  private final AI ai;

  CompetitionAI(AI ai) {
    this.ai = ai;
  }

  public AI getAI() {
    return ai;
  }

  public int getWin() {
    return win;
  }

  public synchronized int incWin() {
    return win++;
  }

  public int getLose() {
    return lose;
  }

  public synchronized int incLose() {
    return lose++;
  }

  public int getDraw() {
    return draw;
  }

  public synchronized int incDraw() {
    return draw++;
  }

  public void recordMoveTimeCost(long ms) {
    moveTimeCostMs.add(ms);
  }

  @Override
  public String toString() {
    synchronized (moveTimeCostMs) {
      return new StringBuilder()
          .append(getAI())
          .append( " ")
          .append(getWin())
          .append(" wins, ")
          .append(getLose())
          .append(" loses, ")
          .append(getDraw())
          .append(" draw, ")
          .append("duration(ms): ")
          .append(moveTimeCostMs.stream()
              .mapToLong(v -> v)
              .summaryStatistics())
          .toString();
    }
  }
}
