package common.boardclass.testing;

import common.Constants;
import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardFactories;
import common.pattern.Pattern;
import model.GameBoard;

/**
 * Utility method that parses game boards.
 */
public class BoardClassUtil {

  public static final char EMPTY_CHAR = '\u253C';
  public static final char BLACK_CHAR = '\u25CF';
  public static final char WHITE_CHAR = '\u25CB';

  public static final StoneType E = StoneType.NOTHING;
  public static final StoneType W = StoneType.WHITE;
  public static final StoneType B = StoneType.BLACK;
  public static final GameBoard EMPTY_GAME_BOARD = new GameBoard() {
    @Override
    public StoneType get(int i, int j) {
      return StoneType.NOTHING;
    }

    @Override
    public boolean isFull() {
      return false;
    }

    @Override
    public boolean isEmpty() {
      return true;
    }

    @Override
    public GameBoard withPositionSet(int i, int j, StoneType stoneType) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean wins(StoneType stoneType) {
      return false;
    }

    @Override
    public int getStoneCount() {
      return 0;
    }
  };

  public static BoardClass<Pattern> fromString(String s) {
    final String[] b = s.split("\n");
    return BoardFactories.FOR_PATTERN
        .fromGameBoard(new AbstractGameBoard() {

      @Override
      public StoneType get(int i, int j) {
        switch (b[i].charAt(j)) {
          case 'O':
          case 'B':
          case BLACK_CHAR:
            return StoneType.BLACK;
          case 'X':
          case 'W':
          case WHITE_CHAR:
            return StoneType.WHITE;
          default:
            return StoneType.NOTHING;
        }
      }
    });
  }

  private static GameBoard fromArray(StoneType[][] board) {
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

  public static BoardClass<Pattern> createBoard(StoneType[][] board) {
    return BoardFactories.FOR_PATTERN.fromGameBoard(fromArray(board));
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

    @Override
    public int getStoneCount() {
      int ct = 0;
      for (int i = 0; i < Constants.BOARD_SIZE; i++) {
        for (int j = 0; j < Constants.BOARD_SIZE; j++) {
          if (get(i, j) != StoneType.NOTHING) {
            ct++;
          }
        }
      }
      return ct;
    }
  }
}
