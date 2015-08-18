package ai.minmax;

import com.google.common.collect.Iterables;

import common.Constants;
import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardFactories;
import common.pattern.Pattern;

import java.util.Collection;

import ai.AI;
import ai.candidatemoveselector.CandidateMovesSelector;
import ai.candidatemoveselector.CandidateMovesSelectors;
import ai.minmax.transitiontable.NoopTransitionTable;
import ai.minmax.transitiontable.TransitionTable;
import ai.minmax.transitiontable.TransitionTableImpl;
import model.GameBoard;
import model.Position;

public class MinMaxSearch<T extends Pattern> implements AI {

  private final int maxDepth;
  private final TransitionTable.Factory<MinMaxNode> transitionTableFactory;
  private final String name;
  private final boolean alphaBetaPruning;
  private final Evaluator<T> evaluator;
  private final CandidateMovesSelector<T> candidateMovesSelector;
  private final BoardClass.Factory<T> boardClassFactory;

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
  }

  private static <T extends Pattern> Builder<T> newBuilder() {
    return new Builder<>();
  }

  public static Builder<Pattern> defaultBuilderForPattern() {
    return newBuilder()
        .withCandidateMoveSelector(CandidateMovesSelectors.DEFAULT)
        .withBoardClassFactory(BoardFactories.BOARD_CLASS_WITH_MATCHING_PATTERNS_FACTORY)
        .withEvaluator(new DefaultEvaluator());
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
      result = minMaxSearch(boardClass, Integer.MIN_VALUE, Integer.MAX_VALUE,
          maxDepth, minMax, stoneType, transitionTableFactory.create());
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


  private MinMaxNode save(TransitionTable<MinMaxNode> transitionTable, BoardClass boardClass, MinMaxNode
      node) {
    transitionTable.put(boardClass, node);
    return node;
  }

  private MinMaxNode minMaxSearch(BoardClass<T> boardClass,
                                  int alpha,
                                  int beta,
                                  int depth,
                                  MinMax minMax,
                                  StoneType stoneType,
                                  TransitionTable<MinMaxNode> transitionTable) {
    MinMaxNode fromCache = transitionTable.get(boardClass);
    if (fromCache != null) {
      cacheHit++;
      return fromCache;
    }
    if (boardClass.wins(StoneType.BLACK)) {
      return save(transitionTable, boardClass, new MinMaxNode(null, Integer.MAX_VALUE));
    } else if (boardClass.wins(StoneType.WHITE)) {
      return save(transitionTable, boardClass, new MinMaxNode(null, Integer.MIN_VALUE));
    } else if (boardClass.isFull()) {
      return save(transitionTable, boardClass, new MinMaxNode(null, 0));
    } else if (depth == 0) {
      return save(transitionTable, boardClass, new MinMaxNode(null, eval(boardClass, stoneType)));
    }
    MinMaxNode res = null;
    Collection<Position> candidateMoves =
        candidateMovesSelector.getCandidateMoves(boardClass, minMax.getStoneType());
    if (depth == maxDepth && candidateMoves.size() == 1) {
      return new MinMaxNode(Iterables.getOnlyElement(candidateMoves), 0);
    }
    for (Position position : candidateMoves) {
      int i = position.getRowIndex();
      int j = position.getColumnIndex();
      BoardClass<T> newBoardClass = boardClass.withPositionSet(i, j, minMax.getStoneType());
      if (minMax == MinMax.MAX) {
        int curMax = res == null ? alpha : res.getScore();
        int v = minMaxSearch(newBoardClass, curMax, beta, depth - 1,
            MinMax.MIN, stoneType, transitionTable).getScore();
        res = update(minMax, res, position, v);
        if (alphaBetaPruning && res.getScore() >= beta) {
          return save(transitionTable, boardClass, res);
        }
      } else {
        int curMin = res == null ? beta : res.getScore();
        int v = minMaxSearch(newBoardClass, alpha, curMin, depth - 1,
            MinMax.MAX, stoneType, transitionTable).getScore();
        res = update(minMax, res, position, v);
        if (alphaBetaPruning && res.getScore() <= alpha) {
          return save(transitionTable, boardClass, res);
        }
      }
    }
    return save(transitionTable, boardClass, res);
  }

  private MinMaxNode update(MinMax minMax, MinMaxNode current, Position position, int f) {
    if (current == null) {
      return new MinMaxNode(position, f);
    }
    switch (minMax) {
      case MAX:
        if (f > current.getScore()) {
          return new MinMaxNode(position, f);
        }
        return current;
      case MIN:
        if (f < current.getScore()) {
          return new MinMaxNode(position, f);
        }
        return current;
      default:
        throw new IllegalArgumentException();
    }
  }

  public static class Builder<T extends Pattern> {

    private int maxDepth = 4;
    private String name = "min_max_search";
    private boolean alphaBetaPruning = true;
    private TransitionTable.Factory<MinMaxNode> transitionTableFactory = TransitionTableImpl::new;
    private CandidateMovesSelector<T> candidateMoveSelector;
    private BoardClass.Factory<T> boardClassFactory;
    private Evaluator<T> evaluator;

    private Builder() {
    }

    public MinMaxSearch<T> build() {
      return new MinMaxSearch<>(this);
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
}
