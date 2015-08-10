package common;

public enum PlayerType {
  HUMAN("Human"),
  AI("AI");

  private final String name;

  PlayerType(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
