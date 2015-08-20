package common.boardclass.basic;

import common.StoneType;
import common.boardclass.AbstractBoardClass;
import common.boardclass.BoardClass;
import common.pattern.Pattern;
import model.GameBoard;

/**
 * Implements board class with matching basic pattern.
 */
final class BoardClassWithMatchingPatterns extends AbstractBoardClass<Pattern> {

  private static final Patterns PATTERNS = new Patterns();

  private BoardClassWithMatchingPatterns(
      BoardClassWithMatchingPatterns boardClass, int i, int j, StoneType stoneType) {
    super(boardClass, i, j, stoneType);
  }

  BoardClassWithMatchingPatterns(GameBoard gameBoard) {
    super(gameBoard);
  }

  @Override
  public Pattern.Corpus<Pattern> getCorpus() {
    return PATTERNS;
  }

  @Override
  public BoardClass<Pattern> withPositionSet(int i, int j, StoneType stoneType) {
    return new BoardClassWithMatchingPatterns(this, i, j, stoneType);
  }
}
