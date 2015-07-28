package game;

/**
 * Implementation of game board.
 */
public class GameBoardImpl implements GameBoard {

  private final Square[][] board;

  GameBoardImpl() {
    board = new Square[Constants.ROW_NUM][Constants.COL_NUM];
  }

  @Override
  public Square get(Position position) {
    return board[position.getRowIndex()][position.getColumnIndex()];
  }

  @Override
  public void set(Position position, Square square) {
    board[position.getRowIndex()][position.getColumnIndex()] = square;
  }
}
