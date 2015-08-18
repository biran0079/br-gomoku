package ai.candidatemoveselector;

import common.StoneType;
import common.boardclass.BoardClass;
import model.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Builder for CandidateMovesSelector.
 */
public class CandidateMovesSelectorBuilder {

  private final List<MoveSelector> moveSelectors = new ArrayList<>();

  public CandidateMovesSelectorBuilder add(MoveSelector moveSelector) {
    moveSelectors.add(moveSelector);
    return this;
  }

  public CandidateMovesSelector build() {
    return (boardClass, stoneType) -> {
      for (MoveSelector moveSelector : moveSelectors) {
        Collection<Position> candidateMoves = moveSelector.select(boardClass, stoneType);
        if (!candidateMoves.isEmpty()) {
          return candidateMoves;
        }
      }
      throw new IllegalStateException("No move is selected");
    };
  }

  public interface MoveSelector {

    Collection<Position> select(BoardClass<?> boardClass, StoneType stoneType);
  }
}
