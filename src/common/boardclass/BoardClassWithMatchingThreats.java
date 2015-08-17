package common.boardclass;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import common.PatternType;
import common.StoneType;
import common.pattern.Threat;
import common.pattern.Threats;
import model.GameBoard;

import java.util.Set;

/**
 * TODO share code with BoardClassWithMatchingPatterns.
 */
public class BoardClassWithMatchingThreats extends AbstractBoardClass<Threat> {

  private static final Threats THREATS_WITH_INDEX = new Threats();
  private static final BoardClassWithMatchingThreats EMPTY_BOARD = new BoardClassWithMatchingThreats();
  private final Set<Threat> matchingThreats;

  private BoardClassWithMatchingThreats() {
    super();
    this.matchingThreats = computeMatchingPatterns();
  }

  public BoardClassWithMatchingThreats(BoardClassWithMatchingThreats boardClass,
                                       int i, int j, StoneType stoneType) {
    super(boardClass, i, j, stoneType);
    this.matchingThreats = ImmutableSet.<Threat>builder()
        .addAll(Iterables.filter(
            boardClass.matchingThreats, p -> p.matches(this)))
        .addAll(Iterables.filter(
            THREATS_WITH_INDEX.get(i, j, stoneType), p -> p.matches(this)))
        .build();
  }

  private BoardClassWithMatchingThreats(GameBoard gameBoard) {
    super(gameBoard);
    this.matchingThreats = computeMatchingPatterns();
  }


  private Set<Threat> computeMatchingPatterns() {
    ImmutableSet.Builder<Threat> builder = ImmutableSet.builder();
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE}) {
      for (PatternType patternType : PatternType.values()) {
        builder.addAll(Iterables.filter(
            THREATS_WITH_INDEX.get(stoneType, patternType), p -> p.matches(this)));
      }
    }
    return builder.build();
  }

  @Override
  public boolean matchesAny(StoneType stoneType, PatternType patternType) {
    return !Iterables.isEmpty(getMatchingPatterns(stoneType, patternType));
  }

  @Override
  public Iterable<Threat> getMatchingPatterns(StoneType stoneType, PatternType patternType) {
    return Sets.intersection(matchingThreats, THREATS_WITH_INDEX.get(stoneType, patternType));
  }

  @Override
  public BoardClassWithMatchingThreats withPositionSet(int i, int j, StoneType stoneType) {
    return new BoardClassWithMatchingThreats(this, i, j, stoneType);
  }

  public Set<Threat> getMatchingThreat() {
    return matchingThreats;
  }

  public static class Factory implements BoardClass.Factory<BoardClassWithMatchingThreats> {
    @Override
    public BoardClassWithMatchingThreats fromGameBoard(GameBoard gameBoard) {
      if (gameBoard instanceof BoardClassWithMatchingThreats) {
        return (BoardClassWithMatchingThreats) gameBoard;
      }
      return new BoardClassWithMatchingThreats(gameBoard);
    }

    @Override
    public BoardClassWithMatchingThreats getEmptyBoard() {
      return EMPTY_BOARD;
    }
  }
}
