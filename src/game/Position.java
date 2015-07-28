package game;

public class Position {

	private final int i;
	private final int j;

	private Position(int i, int j) {
		this.i = i;
		this.j = j;
	}

	public static Position create(int i, int j) throws InvalidPositionException {
		if (!isPositionValid(i, j)) {
			throw new InvalidPositionException();
		}
		return new Position(i, j);
	}

	private static boolean isPositionValid(int i, int j) {
		return i >= 0 && i < Constants.ROW_NUM && j >= 0 && j < Constants.COL_NUM;
	}

	public int getRowIndex() {
		return i;
	}

	public int getColumnIndex() {
		return j;
	}
}