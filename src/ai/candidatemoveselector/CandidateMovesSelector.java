package ai.candidatemoveselector;

import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.Pattern;

import java.util.Collection;

import model.Position;

/**
 * Class that selects candidate moves.
 */
public interface CandidateMovesSelector<T extends Pattern> {

  Collection<Position> getCandidateMoves(BoardClass<T> boardClass, StoneType stoneType);

}
