package common.boardclass;

import common.Constants;
import common.PositionTransformer;
import common.StoneType;
import model.GameBoard;

import java.util.Arrays;
import java.util.Random;

/**
 * Implement board efficiently with bit wise operations.
 */
public class BitBoard {

  private static final int MASK_BITS = 3;

  private final int[] board;
  private final int stoneCount;
  private final int hashValue;

  private static final int[][][] ZOBRIST = initializeZobristHash();

  private static final int[][][] initializeZobristHash() {
    Random random = new Random();
    int[][][] a = new int[2][Constants.BOARD_SIZE * 2 - 1][Constants.BOARD_SIZE];
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a[i].length; j++) {
        for (int k = 0; k < a[i][j].length; k++) {
          a[i][j][k] = random.nextInt();
        }
      }
    }
    return a;
  }

  private BitBoard(GameBoard gameBoard, PositionTransformer transformer) {
    this.board = new int[transformer.getBoardRowNumber()];
    int h = 0;
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        StoneType stoneType = gameBoard.get(i, j);
        if (stoneType != StoneType.NOTHING) {
          int localI = transformer.getI(i, j);
          int localJ = transformer.getJ(i, j);
          setBits(board, localI, localJ, stoneType);
          h ^= ZOBRIST[stoneType == StoneType.BLACK ? 0 : 1][localI][localJ];
        }
      }
    }
    this.hashValue = h;
    this.stoneCount = gameBoard.getStoneCount();
  }

  private BitBoard(BitBoard bitBoard, int i, int j, StoneType stoneType) {
    this.board = Arrays.copyOf(bitBoard.board, bitBoard.board.length);
    setBits(this.board, i, j, stoneType);
    this.stoneCount = bitBoard.stoneCount + 1;
    this.hashValue =
        bitBoard.hashValue ^ ZOBRIST[stoneType == StoneType.BLACK ? 0 : 1][i][j];
  }

  public static BitBoard fromGameBoard(GameBoard gameBoard, PositionTransformer transformer) {
    return new BitBoard(gameBoard, transformer);
  }

  public BitBoard set(int i, int j, StoneType stoneType) {
    if (Constants.DEBUG && stoneType == StoneType.NOTHING) {
      throw new IllegalStateException("Cannot set nothing.");
    }
    return new BitBoard(this, i, j, stoneType);
  }

  public StoneType get(int i, int j) {
    return StoneType.fromBits((board[i] >> (j * 2)) & MASK_BITS);
  }

  public int getRow(int i) {
    return board[i];
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        sb.append(get(i, j));
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  @Override
  public int hashCode() {
    return hashValue;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof BitBoard && Arrays.equals(board, ((BitBoard) o).board);
  }

  private static void setBits(int[] board, int i, int j, StoneType stoneType) {
    if (Constants.DEBUG && ((board[i] >> (j * 2)) & 3) != 0) {
      throw new IllegalArgumentException("Cannot set an non-empty position on board!");
    }
    board[i] |= (stoneType.getBits() << (j * 2));
  }

  public int getStoneCount() {
    return stoneCount;
  }
}
