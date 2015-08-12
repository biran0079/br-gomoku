package ai.competition;

import ai.AI;
import com.google.common.math.DoubleMath;

import java.util.Collections;
import java.util.Vector;

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

  public double getMeanMoveMs() {
    return DoubleMath.mean(moveTimeCostMs);
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append(getAI())
        .append( " ")
        .append(getWin())
        .append(" wins, ")
        .append(getLose())
        .append(" loses, ")
        .append(getDraw())
        .append(" draw, ")
        .append("average move duration: ")
        .append(String.format("%.2f [%.2f, %.2f]",
            getMeanMoveMs() / 1000,
            Collections.min(moveTimeCostMs) * 1.0 / 1000,
            Collections.max(moveTimeCostMs) * 1.0 / 1000))
        .append(" sec.")
        .toString();
  }
}
