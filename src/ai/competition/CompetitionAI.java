package ai.competition;

import ai.AI;

/**
 * Holder of competition AI and competition results for the AI.
 */
public interface CompetitionAI {

  AI getAI();

  int getWin();

  int incWin();

  int getLose();

  int incLose();

  int getDraw();

  int incDraw();

  void recordMoveTimeCost(long ms);

  double getMeanMoveMs();

  static CompetitionAI create(AI ai) {
    return new CompetitionAIImpl(ai);
  }
}
