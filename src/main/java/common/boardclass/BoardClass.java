package common.boardclass;

import common.PositionTransformer;
import common.StoneType;
import common.pattern.Pattern;
import common.pattern.PatternType;
import model.GameBoard;
import model.Position;

import java.util.Set;

/**
 * Interface for a equivalent class of game board.
 */
public interface BoardClass<T extends Pattern> extends GameBoard {

  boolean matchesAny(StoneType stoneType, PatternType patternType);

  Set<T> getMatchingPatterns(StoneType stoneType, PatternType patternType);

  Set<T> filterMatching(Set<T> candidate);

  Pattern.Corpus<T> getCorpus();

  @Override
  BoardClass<T> withPositionSet(int i, int j, StoneType stoneType);

  default BoardClass<T> withPositionSet(Position p, StoneType stoneType) {
    return withPositionSet(p.getRowIndex(), p.getColumnIndex(), stoneType);
  }

  BitBoard getBoard(PositionTransformer transformer);

  interface Factory<T extends Pattern> extends GameBoard.Factory {

    BoardClass<T> fromGameBoard(GameBoard gameBoard);

    @Override
    BoardClass<T> getEmptyBoard();
  }
}
