package ai.candidatemoveselector;

import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.Pattern;
import model.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Builder for CandidateMovesSelector.
 */
public class CandidateMovesSelectorBuilder<T extends Pattern> {

  private final List<MoveSelector<T>> moveSelectors = new ArrayList<>();

  private CandidateMovesSelectorBuilder() {}

  public static <T extends Pattern> CandidateMovesSelectorBuilder<T> newBuilder() {
    return new CandidateMovesSelectorBuilder<>();
  }

  public CandidateMovesSelectorBuilder<T> add(MoveSelector<T> moveSelector) {
    moveSelectors.add(moveSelector);
    return this;
  }

  public CandidateMovesSelector<T> build() {
    return (boardClass, stoneType) -> {
      for (MoveSelector<T> moveSelector : moveSelectors) {
        Collection<Position> candidateMoves = moveSelector.select(boardClass, stoneType);
        if (!candidateMoves.isEmpty()) {
          return candidateMoves;
        }
      }
      throw new IllegalStateException("No move is selected");
    };
  }

  public interface MoveSelector<T extends Pattern> {

    Collection<Position> select(BoardClass<T> boardClass, StoneType stoneType);
  }
}
