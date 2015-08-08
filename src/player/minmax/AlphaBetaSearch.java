package player.minmax;

import common.Constants;
import common.Square;
import common.Utils;
import model.GameBoard;
import model.Position;
import player.Player;

import java.util.HashSet;
import java.util.Set;

public class AlphaBetaSearch implements Player {

  private int maxDepth = 2;
  private String name;
  private final Square stoneType;
  private int evalCount;

  public AlphaBetaSearch(String s, Square stoneType) {
    this.name = s;
    this.stoneType = stoneType;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public Position makeMove(GameBoard gameBoard) {
    evalCount = 0;
    Square[][] board = gameBoard.toArray();
    Node res;
    if (stoneType == Square.BLACK_PIECE) {
      res = minMaxSearch(board, Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth, MinMax.MAX);
    } else {
      res = minMaxSearch(board, Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth, MinMax.MIN);
    }
    System.err.println("eval called " + evalCount + " times.");
    return res.p;
  }

  @Override
  public Square getStoneType() {
    return stoneType;
  }

  private int eval(Square[][] board) {
    evalCount++;
    double Ba3 = numOfActive(board,
        Square.BLACK_PIECE, 3), Wa3 = numOfActive(board,
        Square.WHITE_PIECE, 3), Ba4 = numOfActive(board,
        Square.BLACK_PIECE, 4), Wa4 = numOfActive(board,
        Square.WHITE_PIECE, 4);
    return (int) (10 * (Ba3 - Wa3) + 40 * (Ba4 - Wa4));
  }

  private int numOfActive(Square[][] board, Square piece, int n) {
    int R = Constants.BOARD_SIZE, C = Constants.BOARD_SIZE;
    int res = 0;
    int deadEnd, l;
    int[][] d = {{0, 1}, {1, 1}, {1, 0}, {1, -1}};
    for (int i = 1; i < R - 1; i++)
      for (int j = 1; j < C - 1; j++) {
        for (int k = 0; k < 4; k++) {
          if (Utils.isValidPosition(i + n * d[k][0], j + n
              * d[k][1])) {
            deadEnd = 2;
            if (board[i - d[k][0]][j - d[k][1]] != Square.NOTHING) deadEnd--;
            if (board[i + n * d[k][0]][j + n * d[k][1]] == Square.NOTHING) deadEnd--;
            if (deadEnd == 2) continue;
            for (l = 0; l < n; l++)
              if (board[i + l * d[k][0]][j + l * d[k][1]] != piece)
                break;
            if (l == n) {
              res += deadEnd == 0 ? 2 : 1;
            }
          }
        }
      }
    return res;
  }

  private Iterable<Position> getCandidateMoves(Square[][] board, Square stoneType) {
    int[][] d = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0},
        {-1, -1}, {0, -1}, {1, -1}};
    Set<Position> result = new HashSet<Position>();
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        if (board[i][j] != Square.NOTHING) {
          for (int k = 0; k < d.length; k++) {
            int ti = i + d[k][0], tj = j + d[k][1];
            if (Utils.isValidPosition(ti, tj) && board[ti][tj] == Square.NOTHING) {
              Position pos = Position.create(ti, tj);
              result.add(pos);
            }
          }
        }
      }
    }
    if (result.isEmpty()) {
      result.add(Position.create(Constants.BOARD_SIZE / 2, Constants.BOARD_SIZE / 2));
    }
    return result;
  }

  private enum MinMax {
    MIN(Square.WHITE_PIECE),
    MAX(Square.BLACK_PIECE);

    private final Square stoneType;

    MinMax(Square stoneType) {
      this.stoneType = stoneType;
    }

    public Node update(Node current, Position position, int f) {
      if (current == null) {
        return new Node(position, f);
      }
      switch (this) {
        case MAX:
          if (f > current.f) {
            return new Node(position, f);
          }
          return current;
        case MIN:
          if (f < current.f) {
            return new Node(position, f);
          }
          return current;
        default:
          throw new IllegalArgumentException();
      }
    }

    public Square getStoneType() {
      return stoneType;
    }
  }

  private Node minMaxSearch(Square[][] board, int alpha, int beta, int depth, MinMax minMax) {
    if (Utils.playerWins(board, MinMax.MAX.getStoneType())) {
      return new Node(null, Integer.MAX_VALUE);
    } else if (Utils.playerWins(board, MinMax.MIN.getStoneType())) {
      return new Node(null, Integer.MIN_VALUE);
    } else if (depth == 0) {
      return new Node(null, eval(board));
    }

    Node res = null;
    for (Position position : getCandidateMoves(board, minMax.getStoneType())) {
      int i = position.getRowIndex(), j = position.getColumnIndex();
      try {
        board[i][j] = minMax.getStoneType();
        if (minMax == MinMax.MAX) {
          int curMax = res == null ? alpha : res.f;
          int v = minMaxSearch(board, curMax, beta, depth - 1, MinMax.MIN).f;
          res = minMax.update(res, position, v);
          if (res.f >= beta) {
            return res;
          }
        } else {
          int curMin = res == null ? beta : res.f;
          res = minMax.update(res,
              position, minMaxSearch(board, alpha, curMin, depth - 1, MinMax.MAX).f);
          if (res.f <= alpha) {
            return res;
          }
        }
      } finally {
        board[i][j] = Square.NOTHING;
      }
    }
    return res;
  }

  private static class Node {
    private final Position p;
    private final int f;

    Node(Position p, int f) {
      this.p = p;
      this.f = f;
    }
  }
}
