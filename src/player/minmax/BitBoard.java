package player.minmax;

import common.Constants;
import common.Square;
import model.GameBoard;
import model.Position;

import java.util.Arrays;

/**
 * Implement board efficiently with bit wise operations.
 */
public class BitBoard {

  private static final int EMPTY_BITS = 0;
  private static final int BLACK_BITS = 1;
  private static final int WHITE_BITS = 2;
  private static final int MASK_BITS = 3;

  private final int[] board;

  BitBoard(int rowNumver) {
    board = new int[rowNumver];
  }

  private BitBoard(GameBoard gameBoard, PositionTransformer transformer) {
    this(transformer.getBoardRowNumber());
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        Square square = gameBoard.get(Position.create(i, j));
        if (square != Square.NOTHING) {
          setBits(board, transformer.getI(i, j), transformer.getJ(i, j), square);
        }
      }
    }
  }

  private BitBoard(BitBoard bitBoard) {
    board = Arrays.copyOf(bitBoard.board, bitBoard.board.length);
  }

  static int getBits(Square square) {
    switch (square) {
      case BLACK_PIECE:
        return BLACK_BITS;
      case WHITE_PIECE:
        return WHITE_BITS;
      case NOTHING:
        return 0;
      default:
        throw new IllegalArgumentException();
    }
  }

  BitBoard set(int i, int j, Square stoneType) {
    BitBoard result = new BitBoard(this);
    setBits(result.board, i, j, stoneType);
    return result;
  }

  Square get(int i, int j) {
    int bits = (board[i] >> (j * 2)) & MASK_BITS;
    switch (bits) {
      case BLACK_BITS:
        return Square.BLACK_PIECE;
      case WHITE_BITS:
        return Square.WHITE_PIECE;
      case EMPTY_BITS:
        return Square.NOTHING;
      default:
        throw new IllegalStateException("Invalid game board.");
    }
  }

  int getRow(int i) {
    return board[i];
  }

  static BitBoard fromGameBoard(GameBoard gameBoard, PositionTransformer transformer) {
    return new BitBoard(gameBoard, transformer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        switch (get(i, j)) {
          case BLACK_PIECE:
            sb.append("O");
            break;
          case WHITE_PIECE:
            sb.append("X");
            break;
          default:
            sb.append("_");
            break;
        }
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  @Override
  public int hashCode() {
    int res = 0;
    for (int t : board) {
      res *= 121;
      res += t;
    }
    return res;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof BitBoard)) {
      return false;
    }
    return Arrays.equals(board, ((BitBoard) o).board);
  }

  static void setBits(int[] board, int i, int j, Square stoneType) {
    if (((board[i] >> (j * 2)) & 3) != 0) {
      System.err.println("!!");
    }
    board[i] |= (getBits(stoneType) << (j * 2));
  }
}
