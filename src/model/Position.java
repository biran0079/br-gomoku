package model;

import common.Constants;
import common.PositionTransformer;

public class Position implements Comparable<Position> {

	private final int i;
	private final int j;

  private static final Position[][] cached;

  static {
    // row index can be up to 2 * board size - 1 because we sometimes rotate board by 45 degree.
    cached = new Position[2 * Constants.BOARD_SIZE - 1][Constants.BOARD_SIZE];
    for (int i = 0; i < 2 * Constants.BOARD_SIZE - 1; i++)
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        cached[i][j] = new Position(i, j);
      }
  }

	private Position(int i, int j) {
		this.i = i;
		this.j = j;
	}

	public static Position create(int i, int j) {
    if (i < 0 || j < 0) {
      throw new IllegalArgumentException("Cannot have negative position index.");
    }
		return cached[i][j];
	}

  public static boolean isValid(int i, int j) {
    return i >= 0 && i < Constants.BOARD_SIZE && j >= 0 && j < Constants.BOARD_SIZE;
  }

  public Position transform(PositionTransformer transformer) {
    if (transformer == PositionTransformer.IDENTITY) {
      return this;
    }
    return create(transformer.getI(i, j), transformer.getJ(i, j));
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
    return linearIndex();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Position)) {
      return false;
    }
    Position p = (Position) o;
    return p.i == i && p.j == j;
  }

  @Override
  public int compareTo(Position p) {
    return linearIndex() - p.linearIndex();
  }

  private int linearIndex() {
    return i * Constants.BOARD_SIZE + j;
  }
}