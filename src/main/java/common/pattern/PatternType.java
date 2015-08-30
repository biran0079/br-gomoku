package common.pattern;

/**
 * Type of patterns.
 */
public enum PatternType {
  THREE(2),
  FOUR(1),
  STRAIT_FOUR(1),
  FIVE(0),
  GOAL(0);

  private final int threatLevel;

  PatternType(int threatLevel) {
    this.threatLevel = threatLevel;
  }

  public int getThreatLevel() {
    return threatLevel;
  }
}
