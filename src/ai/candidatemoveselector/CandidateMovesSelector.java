package ai.candidatemoveselector;

import static java.util.Collections.singleton;

import common.Constants;
import common.StoneType;
import common.boardclass.BoardClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import model.Position;

/**
 * Class that selects candidate moves.
 */
public interface CandidateMovesSelector {

  Collection<Position> getCandidateMoves(BoardClass boardClass, StoneType stoneType);

}
