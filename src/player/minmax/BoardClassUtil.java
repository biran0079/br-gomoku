package player.minmax;

import common.Square;
import model.GameBoard;
import model.Position;

/**
 * Created by biran on 8/9/2015.
 */
public class BoardClassUtil {

  public static final Square E = Square.NOTHING;
  public static final Square W = Square.WHITE_PIECE;
  public static final Square B = Square.BLACK_PIECE;

  public static GameBoard parseGameBoard(String s) {
    final String[] b = s.split("\n");
    return new GameBoard() {
      @Override
      public Square get(Position position) {
        switch (b[position.getRowIndex()].charAt(position.getColumnIndex())) {
          case 'O':
            return Square.BLACK_PIECE;
          case 'X':
            return Square.WHITE_PIECE;
          default:
            return Square.NOTHING;
        }
      }

      @Override
      public void set(Position position, Square square) {
        throw new UnsupportedOperationException();
      }

      @Override
      public Square[][] toArray() {
        throw new UnsupportedOperationException();
      }

      @Override
      public void initialize() {
        throw new UnsupportedOperationException();
      }

      @Override
      public boolean isFull() {
        throw new UnsupportedOperationException();
      }
    };
  }

  public static BoardClass createBoard(Square[][] board) {
    BoardClass boardClass = BoardClass.emptyBoardClass();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] != Square.NOTHING) {
          boardClass = boardClass.set(i, j, board[i][j]);
        }
      }
    }
    return boardClass;
  }
}
