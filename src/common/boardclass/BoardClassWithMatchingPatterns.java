package common.boardclass;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import com.google.common.collect.Sets;
import common.PatternType;
import common.StoneType;
import common.pattern.Pattern;
import common.pattern.PatternsWithIndex;

import java.util.Set;

import model.GameBoard;

/**
 * BoardClass with matching patterns pre-computed.
 */
class BoardClassWithMatchingPatterns extends AbstractBoardClass<Pattern> {

  private static final PatternsWithIndex PATTERNS_WITH_INDEX = new PatternsWithIndex();
  private static final BoardClassWithMatchingPatterns EMPTY_BOARD = new BoardClassWithMatchingPatterns();

  private final Set<Pattern> matchingPatterns;

  private BoardClassWithMatchingPatterns() {
    super();
    this.matchingPatterns = computeMatchingPatterns();
  }

  private BoardClassWithMatchingPatterns(BoardClassWithMatchingPatterns boardClass,
                                         int i,
                                         int j,
                                         StoneType stoneType) {
    super(boardClass, i, j, stoneType);
    this.matchingPatterns = ImmutableSet.<Pattern>builder()
        .addAll(Iterables.filter(
            boardClass.matchingPatterns, p -> p.matches(this)))
        .addAll(Iterables.filter(
            PATTERNS_WITH_INDEX.get(i, j, stoneType), p -> p.matches(this)))
        .build();
  }

  private BoardClassWithMatchingPatterns(GameBoard gameBoard) {
    super(gameBoard);
    this.matchingPatterns = computeMatchingPatterns();
  }

  private Set<Pattern> computeMatchingPatterns() {
    ImmutableSet.Builder<Pattern> builder = ImmutableSet.builder();
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE}) {
      for (PatternType patternType : PatternType.values()) {
        builder.addAll(Iterables.filter(
            PATTERNS_WITH_INDEX.get(stoneType, patternType), p -> p.matches(this)));
      }
    }
    return builder.build();
  }

  @Override
  public BoardClass withPositionSet(int i, int j, StoneType stoneType) {
    return new BoardClassWithMatchingPatterns(this, i, j, stoneType);
  }

  @Override
  public boolean matchesAny(StoneType stoneType, PatternType patternType) {
    return !Iterables.isEmpty(getMatchingPatterns(stoneType, patternType));
  }

  @Override
  public Iterable<Pattern> getMatchingPatterns(StoneType stoneType, PatternType patternType) {
    return Sets.intersection(matchingPatterns, PATTERNS_WITH_INDEX.get(stoneType, patternType));
  }

  static class Factory implements BoardClass.Factory<BoardClassWithMatchingPatterns> {

    @Override
    public BoardClassWithMatchingPatterns fromGameBoard(GameBoard gameBoard) {
      if (gameBoard instanceof BoardClassWithMatchingPatterns) {
        return (BoardClassWithMatchingPatterns) gameBoard;
      }
      return new BoardClassWithMatchingPatterns(gameBoard);
    }

    @Override
    public BoardClassWithMatchingPatterns getEmptyBoard() {
      return EMPTY_BOARD;
    }
  }
}
