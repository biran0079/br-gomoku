package common;

import common.boardclass.testing.BoardClassUtil;

public enum StoneType {
	NOTHING(0, "" + BoardClassUtil.EMPTY_CHAR),
  BLACK(1, "" + BoardClassUtil.BLACK_CHAR),
  WHITE(2, "" + BoardClassUtil.WHITE_CHAR);

  private final int bits;
  private final String symbol;

  StoneType(int bits, String symbol) {
    this.bits = bits;
    this.symbol = symbol;
  }

  public int getBits() {
    return bits;
  }

  public static StoneType fromBits(int bits) {
    switch (bits) {
      case 0:
        return NOTHING;
      case 1:
        return BLACK;
      case 2:
        return WHITE;
      default:
        throw new IllegalArgumentException("Invalid bits value: " + bits);
    }
  }

  @Override
  public String toString() {
    return symbol;
  }

  public final StoneType getOpponent() {
    switch (this) {
      case BLACK:
        return WHITE;
      case WHITE:
        return BLACK;
      default:
        throw new IllegalArgumentException("NOTHING does not have opponent.");
    }
  }
}
