package common.boardclass;

import common.StoneType;
import model.GameBoard;

/**
 * Utility method that parses game boards.
 */
public class BoardClassUtil {

  public static final StoneType E = StoneType.NOTHING;
  public static final StoneType W = StoneType.WHITE;
  public static final StoneType B = StoneType.BLACK;
  public static final BoardClass.Factory DEFAULT_FACTORY = new BoardClassImpl.Factory();

  public static BoardClass fromString(String s) {
    final String[] b = s.split("\n");
    return DEFAULT_FACTORY.fromGameBoard(new AbstractGameBoard() {

      @Override
      public StoneType get(int i, int j) {
        switch (b[i].charAt(j)) {
          case 'O':
          case 'B':
            return StoneType.BLACK;
          case 'X':
          case 'W':
            return StoneType.WHITE;
          default:
            return StoneType.NOTHING;
        }
      }
    });
  }

  public static GameBoard fromString(StoneType[][] board) {
    return new AbstractGameBoard() {

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
    return DEFAULT_FACTORY.fromGameBoard(fromString(board));
  }

  private abstract static class AbstractGameBoard implements GameBoard {

    @Override
    public boolean isFull() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
      throw new UnsupportedOperationException();
    }

    @Override
    public GameBoard withPositionSet(int i, int j, StoneType stoneType) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean wins(StoneType stoneType) {
      throw new UnsupportedOperationException();
    }
  }
}
