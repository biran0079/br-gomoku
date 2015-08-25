package ai.minmax;

import ai.AI;
import ai.candidatemoveselector.CandidateMovesSelector;
import ai.candidatemoveselector.CandidateMovesSelectors;
import ai.evaluator.EnhancedPatternEvaluator;
import ai.evaluator.Evaluator;
import ai.evaluator.SimpleThreatEvaluator;
import ai.minmax.transitiontable.NoopTransitionTable;
import ai.minmax.transitiontable.TransitionTable;
import ai.minmax.transitiontable.TransitionTableImpl;

import common.Constants;
import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardFactories;
import common.pattern.Pattern;
import common.pattern.Threat;
import model.GameBoard;
import model.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MinMaxSearch<T extends Pattern> implements AI {

  private static final int MAX_VALUE = 1_000_000;
  private static final int MIN_VALUE = -1_000_000;

  private final int maxDepth;
  private final TransitionTable.Factory<MinMaxNode> transitionTableFactory;
  private final String name;
  private final boolean alphaBetaPruning;
  private final Evaluator<T> evaluator;
  private final CandidateMovesSelector<T> candidateMovesSelector;
  private final BoardClass.Factory<T> boardClassFactory;
  private final boolean useKillerHeuristic;
  private final Algorithm algorithm;

  private int evalCount;
  private int cacheHit;

  private MinMaxSearch(Builder<T> builder) {
    this.name = builder.name;
    this.maxDepth = builder.maxDepth;
    this.alphaBetaPruning = builder.alphaBetaPruning;
    this.transitionTableFactory = builder.transitionTableFactory;
    this.candidateMovesSelector = builder.candidateMoveSelector;
    this.evaluator = builder.evaluator;
    this.boardClassFactory = builder.boardClassFactory;
    this.useKillerHeuristic = builder.useKillerHeuristic;
    this.algorithm = builder.algorithm;
  }

  public static <T extends Pattern> Builder<T> newBuilder() {
    return new Builder<>();
  }

  public static Builder<Pattern> defaultBuilderForPattern() {
    return MinMaxSearch.newBuilder()
        .withCandidateMoveSelector(CandidateMovesSelectors.FOR_PATTERN)
        .withBoardClassFactory(BoardFactories.FOR_PATTERN)
        .withEvaluator(new EnhancedPatternEvaluator());
  }

  public static Builder<Threat> defaultBuilderForThreat() {
    return MinMaxSearch.<Threat>newBuilder()
        .withCandidateMoveSelector(CandidateMovesSelectors.FOR_THREAT)
        .withBoardClassFactory(BoardFactories.FOR_THREAT)
        .withEvaluator(new SimpleThreatEvaluator());
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public Position nextMove(GameBoard gameBoard, StoneType stoneType) {
    evalCount = 0;
    cacheHit = 0;
    BoardClass<T> boardClass = boardClassFactory.fromGameBoard(gameBoard);
    if (boardClass.wins(StoneType.BLACK) || boardClass.wins(StoneType.WHITE)) {
      throw new IllegalStateException("Already won.");
    }
    if (boardClass.isFull()) {
      throw new IllegalStateException("Already won.");
    }
    MinMax minMax = stoneType == StoneType.BLACK ? MinMax.MAX : MinMax.MIN;
    MinMaxNode result;
    try {
      switch (algorithm) {
        case MINMAX:
          result = minMaxSearch(boardClass, MIN_VALUE, MAX_VALUE,
              maxDepth, minMax, stoneType,
              transitionTableFactory.create(),
              new Position[maxDepth + 1]);
          break;
        case NEGAMAX:
          result = negaMax(boardClass, MIN_VALUE, MAX_VALUE,
              maxDepth, minMax, stoneType,
              transitionTableFactory.create(),
              new Position[maxDepth + 1]);
          break;
        case PVS:
          result = pvs(boardClass, MIN_VALUE, MAX_VALUE,
              maxDepth, minMax, stoneType,
              transitionTableFactory.create(),
              new Position[maxDepth + 1]);
          break;
        default:
          throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
      }
      return result.getBestMove();
    } catch (Throwable e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (Constants.DEBUG && result != null) {
        System.err.println(boardClass
            +  name + "\neval result:" + result.getScore() +", eval called " + evalCount
            + " times, cache hit " + cacheHit + " times.");
      }
    }
  }

  private int eval(BoardClass<T> boardClass, StoneType stoneType) {
    evalCount++;
    StoneType nextToMove = maxDepth % 2 == 0 ? stoneType : stoneType.getOpponent();
    return evaluator.eval(boardClass, nextToMove);
  }

  private enum MinMax {
    MIN(StoneType.WHITE, -1),
    MAX(StoneType.BLACK, 1);

    private final StoneType stoneType;
    private final int color;

    MinMax(StoneType stoneType, int color) {
      this.stoneType = stoneType;
      this.color = color;
    }

    public MinMax opposite() {
      return this == MIN ? MAX : MIN;
    }
  }

  private MinMaxNode save(TransitionTable<MinMaxNode> transitionTable,
      BoardClass<T> boardClass, MinMaxNode node) {
    transitionTable.put(boardClass, node);
    return node;
  }

  private MinMaxNode evaluateTerminalNode(
      BoardClass<T> boardClass,
      int depth,
      StoneType stoneType) {
    MinMaxNode result = null;
    if (boardClass.wins(StoneType.BLACK)) {
      result = new MinMaxNode(null, MAX_VALUE);
    } else if (boardClass.wins(StoneType.WHITE)) {
      result = new MinMaxNode(null, MIN_VALUE);
    } else if (boardClass.isFull()) {
      result = new MinMaxNode(null, 0);
    } else if (depth == 0) {
      result = new MinMaxNode(null, eval(boardClass, stoneType));
    }
    return result;
  }

  private Iterable<Position> getCandidateMoves(
      BoardClass<T> boardClass,
      MinMax minMax,
      Position killer) {
    Collection<Position> candidateMoves =
        candidateMovesSelector.getCandidateMoves(boardClass, minMax.stoneType);
    if (useKillerHeuristic) {
      if (killer != null
          && boardClass.get(killer.getRowIndex(), killer.getColumnIndex()) == StoneType.NOTHING) {
        List<Position> l = new ArrayList<>();
        l.add(killer);
        l.addAll(candidateMoves.stream().filter(t -> !t.equals(killer))
            .collect(Collectors.toList()));
        candidateMoves = l;
      }
    }
    return candidateMoves;
  }

  private MinMaxNode minMaxSearch(BoardClass<T> boardClass,
                                  int alpha,
                                  int beta,
                                  int depth,
                                  MinMax minMax,
                                  StoneType stoneType,
                                  TransitionTable<MinMaxNode> transitionTable,
                                  Position[] killers) {
    MinMaxNode fromCache = transitionTable.get(boardClass);
    if (fromCache != null) {
      cacheHit++;
      return fromCache;
    }
    MinMaxNode res = evaluateTerminalNode(boardClass, depth, stoneType);
    if (res != null) {
      return save(transitionTable, boardClass, res);
    }
    int min = MAX_VALUE, max = MIN_VALUE;
    for (Position position : getCandidateMoves(boardClass, minMax, killers[depth])) {
      int i = position.getRowIndex();
      int j = position.getColumnIndex();
      BoardClass<T> newBoardClass = boardClass.withPositionSet(i, j, minMax.stoneType);
      if (minMax == MinMax.MAX) {
        int v = minMaxSearch(newBoardClass, alpha, beta, depth - 1,
            MinMax.MIN, stoneType, transitionTable, killers).getScore();
        alpha = Math.max(alpha, v);
        if (v > max) {
          max = v;
        }
        if (res == null || res.getScore() < max) {
          res = new MinMaxNode(position, max);
        }
        if (alphaBetaPruning && alpha >= beta) {
          if (useKillerHeuristic) {
            killers[depth] = position;
          }
          break;
        }
      } else {
        int v = minMaxSearch(newBoardClass, alpha, beta, depth - 1,
            MinMax.MAX, stoneType, transitionTable, killers).getScore();
        beta = Math.min(beta, v);
        if (v < min) {
          min = v;
        }
        if (res == null || res.getScore() > min) {
          res = new MinMaxNode(position, min);
        }
        if (alphaBetaPruning && alpha >= beta) {
          if (useKillerHeuristic) {
            killers[depth] = position;
          }
          break;
        }
      }
    }
    return save(transitionTable, boardClass, res);
  }

  private MinMaxNode negaMax(BoardClass<T> boardClass,
      int alpha,
      int beta,
      int depth,
      MinMax minMax,
      StoneType stoneType,
      TransitionTable<MinMaxNode> transitionTable,
      Position[] killers) {
    MinMaxNode fromCache = transitionTable.get(boardClass);
    if (fromCache != null) {
      cacheHit++;
      return fromCache;
    }
    MinMaxNode res = evaluateTerminalNode(boardClass, depth, stoneType);
    if (res != null) {
      if (minMax.color == -1) {
        res = new MinMaxNode(res.getBestMove(), -res.getScore());
      }
      return save(transitionTable, boardClass, res);
    }
    int bestScore = MIN_VALUE;
    for (Position position : getCandidateMoves(boardClass, minMax, killers[depth])) {
      int i = position.getRowIndex();
      int j = position.getColumnIndex();
      BoardClass<T> newBoardClass = boardClass.withPositionSet(i, j, minMax.stoneType);
      int v = -negaMax(newBoardClass, -beta, -alpha, depth - 1,
          minMax.opposite(), stoneType, transitionTable, killers).getScore();
      if (res == null || v > bestScore) {
        bestScore = v;
        res = new MinMaxNode(position, v);
      }
      alpha = Math.max(alpha, v);
      if (alphaBetaPruning && alpha >= beta) {
        if (useKillerHeuristic) {
          killers[depth] = position;
        }
        break;
      }
    }
    return save(transitionTable, boardClass, res);
  }

  private MinMaxNode pvs(BoardClass<T> boardClass,
      int alpha,
      int beta,
      int depth,
      MinMax minMax,
      StoneType stoneType,
      TransitionTable<MinMaxNode> transitionTable,
      Position[] killers) {
    MinMaxNode fromCache = transitionTable.get(boardClass);
    if (fromCache != null) {
      cacheHit++;
      return fromCache;
    }
    MinMaxNode res = evaluateTerminalNode(boardClass, depth, stoneType);
    if (res != null) {
      if (minMax.color == -1) {
        res = new MinMaxNode(res.getBestMove(), -res.getScore());
      }
      return save(transitionTable, boardClass, res);
    }
    int bestScore = MIN_VALUE;
    boolean first = true;
    for (Position position : getCandidateMoves(boardClass, minMax, killers[depth])) {
      int i = position.getRowIndex();
      int j = position.getColumnIndex();
      BoardClass<T> newBoardClass = boardClass.withPositionSet(i, j, minMax.stoneType);
      int v;
      if (first) {
        v = -pvs(newBoardClass, -beta, -alpha, depth - 1,
            minMax.opposite(), stoneType, transitionTable, killers).getScore();
        first = false;
      } else {
        v = -pvs(newBoardClass, -alpha - 1, -alpha, depth - 1,
            minMax.opposite(), stoneType, transitionTable, killers).getScore();
        if (alpha < v && v < beta) {
          v = -pvs(newBoardClass, -beta, -v, depth - 1,
              minMax.opposite(), stoneType, transitionTable, killers).getScore();
        }
      }
      if (res == null || v > bestScore) {
        bestScore = v;
        res = new MinMaxNode(position, v);
      }
      alpha = Math.max(alpha, v);
      if (alphaBetaPruning && alpha >= beta) {
        if (useKillerHeuristic) {
          killers[depth] = position;
        }
        break;
      }
    }
    return save(transitionTable, boardClass, res);
  }

  public static class Builder<T extends Pattern> {

    private int maxDepth = 4;
    private String name = "min_max_search";
    private boolean alphaBetaPruning = true;
    private TransitionTable.Factory<MinMaxNode> transitionTableFactory = TransitionTableImpl::new;
    private CandidateMovesSelector<T> candidateMoveSelector;
    private BoardClass.Factory<T> boardClassFactory;
    private Evaluator<T> evaluator;
    private boolean useKillerHeuristic = false;
    private Algorithm algorithm = Algorithm.MINMAX;

    private Builder() {
    }

    public MinMaxSearch<T> build() {
      return new MinMaxSearch<>(this);
    }

    public Builder<T> useKillerHeuristic() {
      useKillerHeuristic = true;
      return this;
    }

    public Builder<T> withAlgorithm(Algorithm algorithm) {
      this.algorithm = algorithm;
      return this;
    }

    public Builder<T> withBoardClassFactory(BoardClass.Factory<T> boardClassFactory) {
      this.boardClassFactory = boardClassFactory;
      return this;
    }

    public Builder<T> withEvaluator(Evaluator<T> evaluator) {
      this.evaluator = evaluator;
      return this;
    }

    public Builder<T> withName(String name) {
      this.name = name;
      return this;
    }

    public Builder<T> withMaxDepth(int maxDepth) {
      this.maxDepth = maxDepth;
      return this;
    }

    public Builder<T> withCandidateMoveSelector(CandidateMovesSelector<T> candidateMoveSelector) {
      this.candidateMoveSelector = candidateMoveSelector;
      return this;
    }

    public Builder<T> withAlphaBetaPruning(boolean alphaBetaPruning) {
      this.alphaBetaPruning = alphaBetaPruning;
      return this;
    }

    public Builder<T> withTransitionTableFactory(
        TransitionTable.Factory<MinMaxNode> transitionTableFactory) {
      this.transitionTableFactory = transitionTableFactory;
      return this;
    }

    public Builder<T> noTransitionTable() {
      return withTransitionTableFactory(NoopTransitionTable::new);
    }
  }

  public enum Algorithm {
    MINMAX,
    NEGAMAX,
    PVS,
  }
}
