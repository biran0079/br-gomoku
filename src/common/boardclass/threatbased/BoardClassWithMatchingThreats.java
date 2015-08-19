package common.boardclass.threatbased;

import common.StoneType;
import common.boardclass.AbstractBoardClass;
import common.pattern.Pattern;
import common.pattern.Threat;
import model.GameBoard;

/**
 * Implements board class with matching threats.
 */
final class BoardClassWithMatchingThreats extends AbstractBoardClass<Threat> {

  private static final Threats THREATS = new Threats();

  private BoardClassWithMatchingThreats(BoardClassWithMatchingThreats boardClass,
      int i, int j, StoneType stoneType) {
    super(boardClass, i, j, stoneType);
  }

  BoardClassWithMatchingThreats(GameBoard gameBoard) {
    super(gameBoard);
  }

  @Override
  protected Pattern.Corpus<Threat> getCorpus() {
    return THREATS;
  }

  @Override
  public BoardClassWithMatchingThreats withPositionSet(int i, int j, StoneType stoneType) {
    return new BoardClassWithMatchingThreats(this, i, j, stoneType);
  }
}
