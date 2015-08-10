package common;

import model.ReadOnlyGameBoard;

/**
 * Utility method that parses game boards.
 */
public class BoardClassUtil {

  public static final StoneType E = StoneType.NOTHING;
  public static final StoneType W = StoneType.WHITE;
  public static final StoneType B = StoneType.BLACK;

  public static BoardClass fromString(String s) {
    final String[] b = s.split("\n");
    return BoardClass.fromGameBoard(new AbstractReadOnlyGameBoard() {

      @Override
      public StoneType get(int i, int j) {
        switch (b[i].charAt(j)) {
          case 'O': case 'B':
            return StoneType.BLACK;
          case 'X': case 'W':
            return StoneType.WHITE;
          default:
            return StoneType.NOTHING;
        }
      }
    });
  }

  public static ReadOnlyGameBoard fromString(StoneType[][] board) {

    return new AbstractReadOnlyGameBoard() {

      @Override
      public StoneType get(int i, int j) {
        if (i >= board.length || j >= board[i].length) {
          return StoneType.NOTHING;
        }
        return board[i][j];
      }
    };
  }

  public static BoardClass createBoard(StoneType[][] board) {
    return BoardClass.fromGameBoard(fromString(board));
  }

  private abstract static class AbstractReadOnlyGameBoard implements ReadOnlyGameBoard {

    @Override
    public boolean isFull() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
      throw new UnsupportedOperationException();
    }
  }
}
