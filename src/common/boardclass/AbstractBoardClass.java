package common.boardclass;

import static common.PositionTransformer.CLOCK_180;
import static common.PositionTransformer.CLOCK_180_M;
import static common.PositionTransformer.CLOCK_270;
import static common.PositionTransformer.CLOCK_270_M;
import static common.PositionTransformer.CLOCK_90;
import static common.PositionTransformer.CLOCK_90_M;
import static common.PositionTransformer.IDENTITY;
import static common.PositionTransformer.IDENTITY_M;
import static common.PositionTransformer.LEFT_DIAGONAL;
import static common.PositionTransformer.RIGHT_DIAGONAL;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import common.Constants;
import common.PositionTransformer;
import common.StoneType;
import common.pattern.Pattern;
import common.pattern.PatternType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import model.GameBoard;

/**
 * Shared logic for all BoardClass implementations.
 */
public abstract class AbstractBoardClass<T extends Pattern>
    implements BoardClass<T> {

  private static final PositionTransformer[] TRACKING_TRANSFORMERS =
      new PositionTransformer[] {
          IDENTITY,
          IDENTITY_M,
          CLOCK_90,
          CLOCK_90_M,
          CLOCK_180,
          CLOCK_180_M,
          CLOCK_270,
          CLOCK_270_M,
          RIGHT_DIAGONAL,
          LEFT_DIAGONAL,
      };

  private final Map<PositionTransformer, BitBoard> map;
  private final Set<T> matchingThreats;

  protected AbstractBoardClass(AbstractBoardClass<T> boardClass,
      int i, int j, StoneType stoneType) {
    map = new EnumMap<>(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      int ti = transformer.getI(i, j);
      int tj = transformer.getJ(i, j);
      map.put(transformer, boardClass.map.get(transformer).set(ti, tj, stoneType));
    }
    this.matchingThreats = ImmutableSet.<T>builder()
        .addAll(Iterables.filter(
            boardClass.matchingThreats, p -> p.matches(this)))
        .addAll(Iterables.filter(
            getCorpus().get(i, j, stoneType), p -> p.matches(this)))
        .build();
  }

  protected AbstractBoardClass(GameBoard gameBoard) {
    map = new EnumMap<>(PositionTransformer.class);
    for (PositionTransformer transformer : TRACKING_TRANSFORMERS) {
      map.put(transformer, BitBoard.fromGameBoard(gameBoard, transformer));
    }
    this.matchingThreats = computeMatchingPatterns();
  }

  private Set<T> computeMatchingPatterns() {
    ImmutableSet.Builder<T> builder = ImmutableSet.builder();
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE}) {
      for (PatternType patternType : PatternType.values()) {
        builder.addAll(Iterables.filter(
            getCorpus().get(stoneType, patternType), p -> p.matches(this)));
      }
    }
    return builder.build();
  }

  @Override
  public boolean matchesAny(StoneType stoneType, PatternType patternType) {
    return !Iterables.isEmpty(getMatchingPatterns(stoneType, patternType));
  }

  @Override
  public Iterable<T> getMatchingPatterns(StoneType stoneType, PatternType patternType) {
    return Sets.intersection(matchingThreats, getCorpus().get(stoneType, patternType));
  }

  protected abstract Pattern.Corpus<T> getCorpus();

  @Override
  public BitBoard getBoard(PositionTransformer transformer) {
    return map.get(transformer);
  }

  @Override
  public boolean isEmpty() {
    return getStoneCount() == 0;
  }

  @Override
  public String toString() {
    return getBoard(PositionTransformer.IDENTITY).toString();
  }

  @Override
  public boolean isFull() {
    return getStoneCount() == Constants.BOARD_SIZE * Constants.BOARD_SIZE;
  }

  @Override
  public boolean wins(StoneType stoneType) {
    return matchesAny(stoneType, PatternType.GOAL);
  }

  @Override
  public int getStoneCount() {
    return getBoard(IDENTITY).getStoneCount();
  }

  @Override
  public StoneType get(int i, int j) {
    return getBoard(IDENTITY).get(i, j);
  }
}
