package model;

import common.Constants;

public class Position {

	private final int i;
	private final int j;

  private static final Position[][] cached;

  static {
    cached = new Position[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
    for (int i = 0; i < Constants.BOARD_SIZE; i++)
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        cached[i][j] = new Position(i, j);
      }
  }

	private Position(int i, int j) {
		this.i = i;
		this.j = j;
	}

	public static Position create(int i, int j) {
		return cached[i][j];
	}

	public int getRowIndex() {
		return i;
	}

	public int getColumnIndex() {
		return j;
	}

  @Override
  public String toString() {
    return "[" + i + ", " + j + "]";
  }

  @Override
  public int hashCode() {
    return i * Constants.BOARD_SIZE + j;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Position)) {
      return false;
    }
    Position p = (Position) o;
    return p.i == i && p.j == j;
  }
}