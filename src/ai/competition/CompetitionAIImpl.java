package ai.competition;

import ai.AI;
import com.google.common.math.DoubleMath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Thread safe implementation of CompetitionAI.
 */
class CompetitionAIImpl implements CompetitionAI {

  private int win, lose, draw;
  private List<Long> moveTimeCostMs = new ArrayList<>();
  private final AI ai;

  CompetitionAIImpl(AI ai) {
    this.ai = ai;
  }

  @Override
  public AI getAI() {
    return ai;
  }

  @Override
  public int getWin() {
    return win;
  }

  @Override
  public synchronized int incWin() {
    return win++;
  }

  @Override
  public int getLose() {
    return lose;
  }

  @Override
  public synchronized int incLose() {
    return lose++;
  }

  @Override
  public int getDraw() {
    return draw;
  }

  @Override
  public synchronized int incDraw() {
    return draw++;
  }

  @Override
  public synchronized void recordMoveTimeCost(long ms) {
    moveTimeCostMs.add(ms);
  }

  @Override
  public synchronized double getMeanMoveMs() {
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
