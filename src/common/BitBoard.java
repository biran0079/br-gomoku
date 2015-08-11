package common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import model.GameBoard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Implement board efficiently with bit wise operations.
 */
public class BitBoard {

  private static final LoadingCache<Integer, BitBoard> EMPTY_BOARD_CACHE =
      CacheBuilder.newBuilder()
          .build(new CacheLoader<Integer, BitBoard>() {
            @Override
            public BitBoard load(Integer boardRowNumber) throws Exception {
              return new BitBoard(boardRowNumber);
            }
          });

  private static final int EMPTY_BITS = 0;
  private static final int BLACK_BITS = 1;
  private static final int WHITE_BITS = 2;
  private static final int MASK_BITS = 3;

  private final int[] board;
  private final int stoneCount;

  private BitBoard(int rowNumber) {
    board = new int[rowNumber];
    stoneCount = 0;
  }

  private BitBoard(GameBoard gameBoard, PositionTransformer transformer) {
    this.board = new int[transformer.getBoardRowNumber()];
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        StoneType stoneType = gameBoard.get(i, j);
        if (stoneType != StoneType.NOTHING) {
          setBits(board, transformer.getI(i, j), transformer.getJ(i, j), stoneType);
        }
      }
    }
    this.stoneCount = gameBoard.getStoneCount();
  }

  private BitBoard(BitBoard bitBoard, int i, int j, StoneType stoneType) {
    this.board = Arrays.copyOf(bitBoard.board, bitBoard.board.length);
    setBits(this.board, i, j, stoneType);
    this.stoneCount = bitBoard.stoneCount + 1;
  }

  public static BitBoard fromGameBoard(GameBoard gameBoard, PositionTransformer transformer) {
    return new BitBoard(gameBoard, transformer);
  }

  public static BitBoard emptyBoard(int boardRowNumber) {
    try {
      return EMPTY_BOARD_CACHE.get(boardRowNumber);
    } catch (ExecutionException e) {
      throw new RuntimeException("should never happen", e);
    }
  }

  public static int getBits(StoneType stoneType) {
    switch (stoneType) {
      case BLACK:
        return BLACK_BITS;
      case WHITE:
        return WHITE_BITS;
      case NOTHING:
        return EMPTY_BITS;
      default:
        throw new IllegalArgumentException();
    }
  }

  public BitBoard set(int i, int j, StoneType stoneType) {
    if (Constants.DEBUG && stoneType == StoneType.NOTHING) {
      throw new IllegalStateException("Cannot set nothing.");
    }
    return new BitBoard(this, i, j, stoneType);
  }

  public StoneType get(int i, int j) {
    int bits = (board[i] >> (j * 2)) & MASK_BITS;
    switch (bits) {
      case BLACK_BITS:
        return StoneType.BLACK;
      case WHITE_BITS:
        return StoneType.WHITE;
      case EMPTY_BITS:
        return StoneType.NOTHING;
      default:
        throw new IllegalStateException("Invalid game board.");
    }
  }

  public int getRow(int i) {
    return board[i];
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        switch (get(i, j)) {
          case BLACK:
            sb.append("O");
            break;
          case WHITE:
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

  static void setBits(int[] board, int i, int j, StoneType stoneType) {
    if (Constants.DEBUG && ((board[i] >> (j * 2)) & 3) != 0) {
      throw new IllegalArgumentException("Cannot set an non-empty position on board!");
    }
    board[i] |= (getBits(stoneType) << (j * 2));
  }

  public boolean isEmpty() {
    for (int row : board) {
      if (row != 0) {
        return false;
      }
    }
    return true;
  }

  public int getStoneCount() {
    return stoneCount;
  }
}
