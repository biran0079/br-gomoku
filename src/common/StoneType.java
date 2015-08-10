package common;

public enum StoneType {
	NOTHING,
  BLACK,
  WHITE;

  public final StoneType getOpponent() {
    switch (this) {
      case BLACK:
        return WHITE;
      case WHITE:
        return BLACK;
      default:
        throw new IllegalArgumentException("NOTHING does not have opponent.a");
    }
  }
}
