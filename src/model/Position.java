package model;

import common.Constants;
import common.InvalidPositionException;
import common.Utils;

public class Position {

	private final int i;
	private final int j;

	private Position(int i, int j) {
		this.i = i;
		this.j = j;
	}

	public static Position create(int i, int j) throws InvalidPositionException {
		if (!Utils.isValidPosition(i, j)) {
			throw new InvalidPositionException();
		}
		return new Position(i, j);
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
    return i * Constants.COL_NUM + j;
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