package player.minmax;

import static player.minmax.PositionTransformer.*;

import com.google.common.collect.Iterables;
import common.Constants;
import common.Square;
import common.Utils;
import model.GameBoard;
import model.Position;
import player.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AlphaBetaSearch implements Player {

  private static final int MAX_DEPTH = 4;
  private final String name;
  private final Square stoneType;
  private final Map<BitBoard, Node> transitionTable = new HashMap<>();

  private int evalCount;
  private int cacheHit;

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
    cacheHit = 0;
    transitionTable.clear();
    if (Utils.playerWins(gameBoard, Square.BLACK_PIECE)
       || Utils.playerWins(gameBoard, Square.WHITE_PIECE)) {
      throw new IllegalStateException("already won");
    }
    BoardClass boardClass = BoardClass.fromGameBoard(gameBoard);
    MinMax minMax = stoneType == Square.BLACK_PIECE ? MinMax.MAX : MinMax.MIN;
    Node res = null;
    try {
      res = minMaxSearch(boardClass, Integer.MIN_VALUE, Integer.MAX_VALUE, MAX_DEPTH, minMax);
      return res.p;
    } catch (Throwable e) {
      e.printStackTrace();
      throw e;
    } finally {
      System.err.println(boardClass +
          "eval result:" + res.f +", eval called " + evalCount + " times, cache hit " + cacheHit + " times.");
    }
  }

  @Override
  public Square getStoneType() {
    return stoneType;
  }

  private int eval(BoardClass boardClass) {
    evalCount++;
    boolean blackMoveNext = stoneType == Square.BLACK_PIECE ? MAX_DEPTH % 2 == 0 : MAX_DEPTH % 2 == 1;
    if (blackMoveNext) {
      if (boardClass.matchesAny(Patterns.BLACK_STRAIT_FOUR)
          || boardClass.matchesAny(Patterns.BLACK_THREE)) {
        return 10;
      } else {
        int ws4 = Iterables.size(boardClass.filterMatchedPatterns(Patterns.WHITE_STRAIT_FOUR));
        int w3 = Iterables.size(boardClass.filterMatchedPatterns(Patterns.WHITE_THREE));
        return -(ws4 + w3);
      }
    } else {
      if (boardClass.matchesAny(Patterns.WHITE_STRAIT_FOUR)
          || boardClass.matchesAny(Patterns.WHITE_THREE)) {
        return -10;
      } else {
        int bs4 = Iterables.size(boardClass.filterMatchedPatterns(Patterns.BLACK_STRAIT_FOUR));
        int b3 = Iterables.size(boardClass.filterMatchedPatterns(Patterns.BLACK_THREE));
        return bs4 + b3;
      }
    }
  }

  Iterable<Position> getCandidateMoves(BoardClass boardClass, Square stoneType) {
    Set<Position> result = new HashSet<>();
    Square opponent = stoneType == Square.WHITE_PIECE ? Square.BLACK_PIECE : Square.WHITE_PIECE;
    for (Pattern p : boardClass.filterMatchedPatterns(Patterns.getThreatPatterns(opponent))) {
      result.addAll(p.getDefensiveMoves());
    }
    if (!result.isEmpty()) {
      return result;
    }
    BitBoard board = boardClass.getBoard(PositionTransformer.IDENTITY);
    int[][] d = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        if (board.get(i, j) != Square.NOTHING) {
          for (int k = 0; k < d.length; k++) {
            int ti = i + d[k][0], tj = j + d[k][1];
            if (Utils.isValidPosition(ti, tj) && board.get(ti, tj) == Square.NOTHING) {
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

    public Square getStoneType() {
      return stoneType;
    }
  }

  private static final PositionTransformer[] IDENTICAL_TRANSFORMERS =
      new PositionTransformer[] {
          IDENTITY,
          IDENTITY_M,
          CLOCK_90,
          CLOCK_90_M,
          CLOCK_180,
          CLOCK_180_M,
          CLOCK_270,
          CLOCK_270_M,
      };

  private Node save(BoardClass boardClass, Node node) {
    for (PositionTransformer transformer : IDENTICAL_TRANSFORMERS) {
      BitBoard bitBoard = boardClass.getBoard(transformer);
      if (!transitionTable.containsKey(bitBoard)) {
        transitionTable.put(bitBoard, node.transform(transformer));
      }
    }
    return node;
  }

  private Node minMaxSearch(BoardClass boardClass, int alpha, int beta, int depth, MinMax minMax) {
    BitBoard identity = boardClass.getBoard(PositionTransformer.IDENTITY);
    if (transitionTable.containsKey(identity)) {
      cacheHit++;
      return transitionTable.get(identity);
    }
    if (boardClass.matchesAny(Patterns.BLACK_GOALS)) {
      return save(boardClass, new Node(null, Integer.MAX_VALUE));
    } else if (boardClass.matchesAny(Patterns.WHITE_GOALS)) {
      return save(boardClass, new Node(null, Integer.MIN_VALUE));
    } else if (depth == 0) {
      return save(boardClass, new Node(null, eval(boardClass)));
    }

    Node res = null;
    for (Position position : getCandidateMoves(boardClass, minMax.getStoneType())) {
      int i = position.getRowIndex(), j = position.getColumnIndex();
      BoardClass newBoardClass = boardClass.set(i, j, minMax.getStoneType());
      if (minMax == MinMax.MAX) {
        int curMax = res == null ? alpha : res.f;
        int v = minMaxSearch(newBoardClass, curMax, beta, depth - 1, MinMax.MIN).f;
        res = update(minMax, res, position, v);
        if (res.f >= beta) {
          return save(boardClass, res);
        }
      } else {
        int curMin = res == null ? beta : res.f;
        int v = minMaxSearch(newBoardClass, alpha, curMin, depth - 1, MinMax.MAX).f;
        res = update(minMax, res, position, v);
        if (res.f <= alpha) {
          return save(boardClass, res);
        }
      }
    }
    return save(boardClass, res);
  }

  public Node update(MinMax minMax, Node current, Position position, int f) {
    if (current == null) {
      return new Node(position, f);
    }
    switch (minMax) {
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
  private static class Node {
    private final Position p;
    private final int f;

    Node(Position p, int f) {
      this.p = p;
      this.f = f;
    }

    Node transform(PositionTransformer transformer) {
      if (p == null || transformer == IDENTITY) {
        return this;
      }
      return new Node(p.transform(transformer), f);
    }

    @Override
    public String toString() {
      return new StringBuilder()
          .append("position: ")
          .append(p)
          .append(", store: ")
          .append(f)
          .toString();
    }
  }
}
