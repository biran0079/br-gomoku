package common.boardclass.basic;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import com.google.common.collect.Sets;
import common.pattern.Pattern;
import common.pattern.PatternType;
import common.StoneType;
import common.boardclass.AbstractBoardClass;
import common.boardclass.BoardClass;

import java.util.Set;

import model.GameBoard;

/**
 * BoardClass with matching patterns pre-computed.
 */
public class BoardClassWithMatchingPatterns extends AbstractBoardClass<Pattern> {

  private static final Patterns PATTERNS = new Patterns();
  private static final BoardClassWithMatchingPatterns EMPTY_BOARD =
      new BoardClassWithMatchingPatterns();

  private final Set<PatternImpl> matchingPatterns;

  private BoardClassWithMatchingPatterns() {
    super();
    this.matchingPatterns = computeMatchingPatterns();
  }

  private BoardClassWithMatchingPatterns(BoardClassWithMatchingPatterns boardClass,
                                         int i,
                                         int j,
                                         StoneType stoneType) {
    super(boardClass, i, j, stoneType);
    this.matchingPatterns = ImmutableSet.<PatternImpl>builder()
        .addAll(Iterables.filter(
            boardClass.matchingPatterns, p -> p.matches(this)))
        .addAll(Iterables.filter(
            PATTERNS.get(i, j, stoneType), p -> p.matches(this)))
        .build();
  }

  private BoardClassWithMatchingPatterns(GameBoard gameBoard) {
    super(gameBoard);
    this.matchingPatterns = computeMatchingPatterns();
  }

  private Set<PatternImpl> computeMatchingPatterns() {
    ImmutableSet.Builder<PatternImpl> builder = ImmutableSet.builder();
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE}) {
      for (PatternType patternType : PatternType.values()) {
        builder.addAll(Iterables.filter(
            PATTERNS.get(stoneType, patternType), p -> p.matches(this)));
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
  public Iterable<PatternImpl> getMatchingPatterns(StoneType stoneType, PatternType patternType) {
    return Sets.intersection(matchingPatterns, PATTERNS.get(stoneType, patternType));
  }

  public static class Factory implements BoardClass.Factory<BoardClass<Pattern>> {

    @Override
    public BoardClass<Pattern> fromGameBoard(GameBoard gameBoard) {
      if (gameBoard instanceof BoardClassWithMatchingPatterns) {
        return (BoardClassWithMatchingPatterns) gameBoard;
      }
      return new BoardClassWithMatchingPatterns(gameBoard);
    }

    @Override
    public BoardClass<Pattern> getEmptyBoard() {
      return EMPTY_BOARD;
    }
  }
}
