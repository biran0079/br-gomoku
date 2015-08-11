package ai.minmax;

import static common.PositionTransformer.CLOCK_180;
import static common.PositionTransformer.CLOCK_180_M;
import static common.PositionTransformer.CLOCK_270;
import static common.PositionTransformer.CLOCK_270_M;
import static common.PositionTransformer.CLOCK_90;
import static common.PositionTransformer.CLOCK_90_M;
import static common.PositionTransformer.IDENTITY;
import static common.PositionTransformer.IDENTITY_M;

import com.google.common.collect.Iterables;

import common.BitBoard;
import common.BoardClass;
import common.Patterns;
import common.PositionTransformer;
import common.StoneType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ai.AI;
import model.Position;
import model.ReadOnlyGameBoard;

public class MinMaxSearch implements AI {

  private final int maxDepth;
  private final Map<BitBoard, Node> transitionTable = new HashMap<>();
  private final CandidateMovesSelector candidateMovesSelector;
  private final String name;
  private final boolean alphaBetaPruning;
  private final boolean useTransitionTable;
  private final Evaluator evaluator;

  private int evalCount;
  private int cacheHit;

  private MinMaxSearch(Builder builder) {
    this.name = builder.name;
    this.maxDepth = builder.maxDepth;
    this.alphaBetaPruning = builder.alphaBetaPruning;
    this.useTransitionTable = builder.useTransitionTable;
    this.candidateMovesSelector = new CandidateMovesSelector(
        builder.randomSampleBranchCandidates);
    this.evaluator = builder.evaluator;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public Position nextMove(ReadOnlyGameBoard gameBoard, StoneType stoneType) {
    evalCount = 0;
    cacheHit = 0;
    transitionTable.clear();
    BoardClass boardClass = BoardClass.fromGameBoard(gameBoard);
    if (boardClass.matchesAny(Patterns.BLACK_GOALS)
        || boardClass.matchesAny(Patterns.WHITE_GOALS)) {
      throw new IllegalStateException("Already won.");
    }
    MinMax minMax = stoneType == StoneType.BLACK ? MinMax.MAX : MinMax.MIN;
    Node res = null;
    try {
      res = minMaxSearch(boardClass, Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth, minMax, stoneType);
      return res.p;
    } catch (Throwable e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (shouldPrintStats()) {
        System.err.println(boardClass +
            "eval result:" + res.f +", eval called " + evalCount + " times, cache hit " + cacheHit + " times.");
      }
    }
  }

  private boolean shouldPrintStats() {
    return false;
  }

  private int eval(BoardClass boardClass, StoneType stoneType) {
    evalCount++;
    boolean blackMoveNext = stoneType == StoneType.BLACK ? maxDepth % 2 == 0 : maxDepth % 2 == 1;
    return evaluator.eval(boardClass, blackMoveNext);
  }

  private enum MinMax {
    MIN(StoneType.WHITE),
    MAX(StoneType.BLACK);

    private final StoneType stoneType;

    MinMax(StoneType stoneType) {
      this.stoneType = stoneType;
    }

    public StoneType getStoneType() {
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

  private Node maybeSave(BoardClass boardClass, Node node) {
    if (useTransitionTable) {
      for (PositionTransformer transformer : IDENTICAL_TRANSFORMERS) {
        BitBoard bitBoard = boardClass.getBoard(transformer);
        if (!transitionTable.containsKey(bitBoard)) {
          transitionTable.put(bitBoard, node.transform(transformer));
        }
      }
    }
    return node;
  }

  private Node minMaxSearch(BoardClass boardClass,
                            int alpha,
                            int beta,
                            int depth,
                            MinMax minMax,
                            StoneType stoneType) {
    BitBoard identity = boardClass.getBoard(PositionTransformer.IDENTITY);
    if (transitionTable.containsKey(identity)) {
      cacheHit++;
      return transitionTable.get(identity);
    }
    if (boardClass.matchesAny(Patterns.BLACK_GOALS)) {
      return maybeSave(boardClass, new Node(null, Integer.MAX_VALUE));
    } else if (boardClass.matchesAny(Patterns.WHITE_GOALS)) {
      return maybeSave(boardClass, new Node(null, Integer.MIN_VALUE));
    } else if (depth == 0) {
      return maybeSave(boardClass, new Node(null, eval(boardClass, stoneType)));
    }

    Node res = null;
    Collection<Position> candidateMoves =
        candidateMovesSelector.getCandidateMoves(boardClass, minMax.getStoneType());
    if (depth == maxDepth && candidateMoves.size() == 1) {
      return new Node(Iterables.getOnlyElement(candidateMoves), 0);
    }
    for (Position position : candidateMoves) {
      int i = position.getRowIndex(), j = position.getColumnIndex();
      BoardClass newBoardClass = boardClass.withPositionSet(i, j, minMax.getStoneType());
      if (minMax == MinMax.MAX) {
        int curMax = res == null ? alpha : res.f;
        int v = minMaxSearch(newBoardClass, curMax, beta, depth - 1, MinMax.MIN, stoneType).f;
        res = update(minMax, res, position, v);
        if (alphaBetaPruning && res.f >= beta) {
          return maybeSave(boardClass, res);
        }
      } else {
        int curMin = res == null ? beta : res.f;
        int v = minMaxSearch(newBoardClass, alpha, curMin, depth - 1, MinMax.MAX, stoneType).f;
        res = update(minMax, res, position, v);
        if (alphaBetaPruning && res.f <= alpha) {
          return maybeSave(boardClass, res);
        }
      }
    }
    return maybeSave(boardClass, res);
  }

  private Node update(MinMax minMax, Node current, Position position, int f) {
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

  public static class Builder {

    private int maxDepth = 4;
    private String name = "min_max_search";
    private int randomSampleBranchCandidates = 0;
    private boolean alphaBetaPruning = true;
    private boolean useTransitionTable = true;
    private Evaluator evaluator = new DefaultEvaluator();

    private Builder() {
    }

    public Builder setEvaluator(Evaluator evaluator) {
      this.evaluator = evaluator;
      return this;
    }

    public MinMaxSearch build() {
      return new MinMaxSearch(this);
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setMaxDepth(int maxDepth) {
      this.maxDepth = maxDepth;
      return this;
    }

    public Builder setRandomSampleBranchCandidates(int randomSampleBranchCandidates) {
      this.randomSampleBranchCandidates = randomSampleBranchCandidates;
      return this;
    }

    public Builder setAlphaBetaPruning(boolean alphaBetaPruning) {
      this.alphaBetaPruning = alphaBetaPruning;
      return this;
    }

    public Builder setUseTransitionTable(boolean useTransitionTable) {
      this.useTransitionTable = useTransitionTable;
      return this;
    }
  }

}
