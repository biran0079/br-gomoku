package ai.competition;

import java.util.Vector;

import ai.AI;

/**
 * Holder of competition AI and competition results for the AI.
 */
class CompetitionAI {

  private volatile int win, lose, draw;
  private final Vector<Long> moveTimeCostMs = new Vector<>();
  private final AI ai;

  CompetitionAI(AI ai) {
    this.ai = ai;
  }

  public AI getAI() {
    return ai;
  }

  int getWin() {
    return win;
  }

  public synchronized void incWin() {
    win++;
  }

  int getLose() {
    return lose;
  }

  public synchronized void incLose() {
    lose++;
  }

  int getDraw() {
    return draw;
  }

  public synchronized void incDraw() {
    draw++;
  }

  public void recordMoveTimeCost(long ms) {
    moveTimeCostMs.add(ms);
  }

  @Override
  public String toString() {
    synchronized (moveTimeCostMs) {
      return getAI() + " " + getWin() + "("
          + String.format("%.2f%%", getWin() * 100.0 / (getDraw() + getWin() + getLose()))
          + ") wins, " + getLose() + " loses, " + getDraw() + " draw, " + "duration(ms): "
          + moveTimeCostMs.stream().mapToLong(v -> v).summaryStatistics();
    }
  }
}
