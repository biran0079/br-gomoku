package ai.minmax;

import ai.AI;
import com.google.common.collect.Iterables;
import common.BoardClass;
import common.Constants;
import common.Patterns;
import common.StoneType;
import model.GameBoard;
import model.Position;

import java.util.Collection;

public class MinMaxSearch implements AI {

  private final int maxDepth;
  private final TransitionTable.Factory transitionTableFactory;
  private final CandidateMovesSelector candidateMovesSelector;
  private final String name;
  private final boolean alphaBetaPruning;
  private final Evaluator evaluator;

  private int evalCount;
  private int cacheHit;

  private MinMaxSearch(Builder builder) {
    this.name = builder.name;
    this.maxDepth = builder.maxDepth;
    this.alphaBetaPruning = builder.alphaBetaPruning;
    this.transitionTableFactory = builder.transitionTableFactory;
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
  public Position nextMove(GameBoard gameBoard, StoneType stoneType) {
    evalCount = 0;
    cacheHit = 0;
    BoardClass boardClass = BoardClass.fromGameBoard(gameBoard);
    if (boardClass.wins(StoneType.BLACK) || boardClass.wins(StoneType.WHITE)) {
      throw new IllegalStateException("Already won.");
    }
    if (boardClass.isFull()) {
      throw new IllegalStateException("Already won.");
    }
    MinMax minMax = stoneType == StoneType.BLACK ? MinMax.MAX : MinMax.MIN;
    MinMaxNode result = null;
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


  private MinMaxNode save(TransitionTable transitionTable, BoardClass boardClass, MinMaxNode node) {
    transitionTable.put(boardClass, node);
    return node;
  }

  private MinMaxNode minMaxSearch(BoardClass boardClass,
                                  int alpha,
                                  int beta,
                                  int depth,
                                  MinMax minMax,
                                  StoneType stoneType,
                                  TransitionTable transitionTable) {
    MinMaxNode fromCache = transitionTable.get(boardClass);
    if (fromCache != null) {
      cacheHit++;
      return fromCache;
    }
    if (boardClass.wins(StoneType.BLACK)) {
      return save(transitionTable, boardClass, new MinMaxNode(null, Integer.MAX_VALUE));
    } else if (boardClass.wins(StoneType.WHITE)) {
      return save(transitionTable, boardClass, new MinMaxNode(null, Integer.MIN_VALUE));
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
      int i = position.getRowIndex(), j = position.getColumnIndex();
      BoardClass newBoardClass = boardClass.withPositionSet(i, j, minMax.getStoneType());
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

  public static class Builder {

    private int maxDepth = 4;
    private String name = "min_max_search";
    private int randomSampleBranchCandidates = 0;
    private boolean alphaBetaPruning = true;
    private Evaluator evaluator = new DefaultEvaluator();
    private TransitionTable.Factory transitionTableFactory =
        () -> new TransitionTableImpl();

    private Builder() {
    }

    public MinMaxSearch build() {
      return new MinMaxSearch(this);
    }

    public Builder withEvaluator(Evaluator evaluator) {
      this.evaluator = evaluator;
      return this;
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withMaxDepth(int maxDepth) {
      this.maxDepth = maxDepth;
      return this;
    }

    public Builder withRandomSampleBranchCandidates(int randomSampleBranchCandidates) {
      this.randomSampleBranchCandidates = randomSampleBranchCandidates;
      return this;
    }

    public Builder withAlphaBetaPruning(boolean alphaBetaPruning) {
      this.alphaBetaPruning = alphaBetaPruning;
      return this;
    }

    public Builder withTransitionTableFactory(
        TransitionTable.Factory transitionTableFactory) {
      this.transitionTableFactory = transitionTableFactory;
      return this;
    }

    public Builder noTransitionTable() {
      return withTransitionTableFactory(() -> new NoopTransitionTable());
    }
  }

}
