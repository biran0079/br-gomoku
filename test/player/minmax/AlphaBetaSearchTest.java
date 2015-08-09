package player.minmax;

import com.google.common.collect.Lists;
import common.Square;
import model.GameBoard;
import model.Position;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by biran on 8/8/2015.
 */
public class AlphaBetaSearchTest {

  AlphaBetaSearch alphaBetaSearch = new AlphaBetaSearch("AI", Square.WHITE_PIECE);

  @Test
  public void getCandidateMoves() {
    List<Position> pos = Lists.newArrayList(alphaBetaSearch.getCandidateMoves(
        createBoard(
            new Square[][]{
                {},
                {W},
                {W},
                {W},
                {W},
            }), Square.BLACK_PIECE));
    assertEquals(2, pos.size());
    assertTrue(pos.contains(Position.create(0, 0)));
    assertTrue(pos.contains(Position.create(5, 0)));
  }

  GameBoard parse(String s) {
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

  private static final Square E = Square.NOTHING;
  private static final Square W = Square.WHITE_PIECE;
  private static final Square B = Square.BLACK_PIECE;

  private BoardClass createBoard(Square[][] board) {
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