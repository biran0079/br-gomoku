package view;

public enum PlayerType {
  HUMAN("Human"),
  AI("AI");

  private final String name;

  PlayerType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  static PlayerType fromName(String text) {
    for (PlayerType type : PlayerType.values()) {
      if (type.getName().equals(text)) {
        return type;
      }
    }
    throw new RuntimeException();
  }
}
