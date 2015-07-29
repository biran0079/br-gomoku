package player;

import game.*;

import java.util.LinkedList;
import java.util.Queue;

public class AlphaBetaSearch implements Player {

  Square[][] board;
  private int maxDepth = 1;
  private int paddingDis = 1;
  private String name;
  private final Square stoneType;

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
    board = Game.getBoard();
    Node res;
    if (stoneType == Square.BLACK_PIECE) {
      res = maxValue(board, -10000000, 10000000, 0);
    } else {
      res = minValue(board, -10000000, 10000000, 0);
    }
    return res.p;
  }

  @Override
  public Square getStoneType() {
    return stoneType;
  }

  private int eval(Square[][] board) {
    double Ba2 = numOfActive(board, Square.BLACK_PIECE, 2), Wa2 = numOfActive(
        board, Square.WHITE_PIECE, 2), Ba3 = numOfActive(board,
        Square.BLACK_PIECE, 3), Wa3 = numOfActive(board,
        Square.WHITE_PIECE, 3), Ba4 = numOfActive(board,
        Square.BLACK_PIECE, 4), Wa4 = numOfActive(board,
        Square.WHITE_PIECE, 4);
    return (int) (10 * (Ba2 - Wa2) + 100 * (Ba3 - Wa3) + 400 * (Ba4 - Wa4));
  }

  private double numOfActive(Square[][] board, Square piece, int n) {
    int R = Constants.ROW_NUM, C = Constants.COL_NUM;
    double res = 0;
    int deadEnd, l;
    int[][] d = {{0, 1}, {1, 1}, {1, 0}, {1, -1}};
    for (int i = 1; i < R - 1; i++)
      for (int j = 1; j < C - 1; j++) {
        for (int k = 0; k < 4; k++) {
          if (Game.validPosition(i + n * d[k][0], j + n
              * d[k][1])) {
            deadEnd = 2;
            if (board[i - d[k][0]][j - d[k][1]] != Square.NOTHING) deadEnd--;
            if (board[i + n * d[k][0]][j + n * d[k][1]] == Square.NOTHING) deadEnd--;
            if (deadEnd == 2) continue;
            for (l = 0; l < n; l++)
              if (board[i + l * d[k][0]][j + l * d[k][1]] != piece)
                break;
            if (l == n) {
              res += deadEnd == 0 ? 1 : 0.4;
            }
          }
        }
      }
    return res;
  }

  private void bfs(Square[][] board, int[][] dis) {
    int R = Constants.ROW_NUM, C = Constants.COL_NUM;
    int[][] d = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0},
        {-1, -1}, {0, -1}, {1, -1}};
    Queue<Position> q = new LinkedList<Position>();
    for (int i = 0; i < R; i++)
      for (int j = 0; j < C; j++)
        if (dis[i][j] == 0)
          q.add(Position.create(i, j));
    if (q.isEmpty()) dis[R / 2][C / 2] = 1;
    while (!q.isEmpty()) {
      Position t = q.poll();
      int i, j;
      for (int k = 0; k < 8; k++) {
        i = t.getRowIndex() + d[k][0];
        j = t.getColumnIndex() + d[k][1];
        if (Game.validPosition(i, j) && dis[i][j] == -1) {
          dis[i][j] = dis[t.getRowIndex()][t.getColumnIndex()] + 1;
          if (dis[i][j] < paddingDis)
            q.add(Position.create(i, j));
        }
      }
    }
  }

  private void printBoard(Square[][] board) {
    int R = Constants.ROW_NUM, C = Constants.COL_NUM;
    for (int i = 0; i < R; i++) {
      for (int j = 0; j < C; j++) {
        if (board[i][j] == Square.NOTHING)
          System.out.print("__");
        else if (board[i][j] == Square.BLACK_PIECE)
          System.out.print("*_");
        else
          System.out.print("o_");
      }
      System.out.println();
    }
    System.out.println();
  }

  private Node maxValue(Square[][] board, int a, int b, int dep) {
    if (Game.playerWins(board, Square.BLACK_PIECE)) {
      return new Node(null, 100000);
    } else if (Game.playerWins(board, Square.WHITE_PIECE)) {
      return new Node(null, -100000);
    } else if (dep > maxDepth) {
      return new Node(null, eval(board));
    }
    int R = Constants.ROW_NUM, C = Constants.COL_NUM;
    int[][] dis = new int[R][C];
    for (int i = 0; i < R; i++)
      for (int j = 0; j < C; j++)
        dis[i][j] = board[i][j] == Square.NOTHING ? -1 : 0;
    bfs(board, dis);
    int f = -10000000, t;
    int pi = -1, pj = -1;
    for (int i = 0; i < R; i++)
      for (int j = 0; j < C; j++) {
        if (dis[i][j] > 0) {
          board[i][j] = Square.BLACK_PIECE;
          t = minValue(board, a, b, dep + 1).f;
          if (t > f) {
            f = t;
            pi = i;
            pj = j;
          }
          board[i][j] = Square.NOTHING;
          if (f >= b) {
            return new Node(Position.create(pi, pj), f);
          }
          a = Math.max(a, f);
        }
      }
    return new Node(Position.create(pi, pj), f);
  }

  private Node minValue(Square[][] board, int a, int b, int dep) {
    if (Game.playerWins(board, Square.BLACK_PIECE)) {
      return new Node(null, 100000);
    } else if (Game.playerWins(board, Square.WHITE_PIECE)) {
      return new Node(null, -100000);
    } else if (dep > maxDepth) {
      return new Node(null, eval(board));
    }
    int R = Constants.ROW_NUM, C = Constants.COL_NUM;
    int[][] dis = new int[R][C];
    for (int i = 0; i < R; i++)
      for (int j = 0; j < C; j++)
        dis[i][j] = board[i][j] == Square.NOTHING ? -1 : 0;
    bfs(board, dis);
    int f = 10000000, t;
    int pi = -1, pj = -1;
    for (int i = 0; i < R; i++)
      for (int j = 0; j < C; j++) {
        if (dis[i][j] > 0) {
          board[i][j] = Square.WHITE_PIECE;
          t = maxValue(board, a, b, dep + 1).f;
          if (t < f) {
            f = t;
            pi = i;
            pj = j;
          }
          board[i][j] = Square.NOTHING;
          if (f <= a) {
            return new Node(Position.create(pi, pj), f);
          }
          b = Math.min(b, f);
        }
      }
    return new Node(Position.create(pi, pj), f);
  }

  class Node {
    Position p;
    int f;

    Node(Position p, int f) {
      this.p = p;
      this.f = f;
    }
  }
}
