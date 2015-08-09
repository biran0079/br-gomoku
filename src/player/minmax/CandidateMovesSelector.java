package player.minmax;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import common.Constants;
import common.Square;
import common.Utils;
import model.Position;

import java.util.*;

/**
 * Class that selects candidate moves.
 */
public class CandidateMovesSelector {

  private Optional<Collection<Position>> immediateDefensiveMoves(BoardClass boardClass, Square stoneType) {
    Square opponent = stoneType == Square.WHITE_PIECE ? Square.BLACK_PIECE : Square.WHITE_PIECE;
    for (Pattern p : boardClass.filterMatchedPatterns(Patterns.getStraitFour(opponent))) {
      return Optional.of(Collections.singleton(p.getDefensiveMoves().get(0)));
    }
    return Optional.empty();
  }

  private Optional<Collection<Position>> regularDefensiveMoves(BoardClass boardClass, Square stoneType) {
    Square opponent = stoneType == Square.WHITE_PIECE ? Square.BLACK_PIECE : Square.WHITE_PIECE;
    List<Pattern> threateningPattern = Lists.newArrayList(
        boardClass.filterMatchedPatterns(Patterns.getThree(opponent)));
    if (!threateningPattern.isEmpty()) {
      Set<Position> union = null;
      for (Pattern p : threateningPattern) {
        if (union == null) {
          union = Sets.newHashSet(p.getDefensiveMoves());
        } else {
          union = Sets.union(union, Sets.newHashSet(p.getDefensiveMoves()));
        }
      }
      return Optional.of(union.isEmpty()
          ? Collections.singleton(threateningPattern.get(0).getDefensiveMoves().get(0))
          : union);
    }
    return Optional.empty();
  }

  private Optional<Collection<Position>> immediateOffensiveMoves(BoardClass boardClass, Square stoneType) {
    for (Pattern p : boardClass.filterMatchedPatterns(Patterns.getStraitFour(stoneType))) {
      return Optional.of(Collections.singleton(p.getDefensiveMoves().get(0)));
    }
    return Optional.empty();
  }

  private Optional<Collection<Position>> regularOffensiveMoves(BoardClass boardClass, Square stoneType) {
    Set<Position> result = new HashSet<>();
    for (Pattern p : boardClass.filterMatchedPatterns(Patterns.getThree(stoneType))) {
      result.addAll(p.getDefensiveMoves());
    }
    if (result.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(result);
  }

  private Collection<Position> getNeighboringMoves(BoardClass boardClass) {
    Set<Position> result = new HashSet<>();
    BitBoard board = boardClass.getBoard(PositionTransformer.IDENTITY);
    int[][] d = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        if (board.get(i, j) != Square.NOTHING) {
          for (int k = 0; k < d.length; k++) {
            int ti = i + d[k][0], tj = j + d[k][1];
            if (Utils.isValidPosition(ti, tj) && board.get(ti, tj) == Square.NOTHING) {
              Position pos = Position.create(ti, tj);
              result.add(pos);
            }
          }
        }
      }
    }
    return result;
  }

  Collection<Position> getCandidateMoves(BoardClass boardClass, Square stoneType) {
    if (boardClass.isEmpty()) {
      return Collections.singleton(Position.create(Constants.BOARD_SIZE / 2, Constants.BOARD_SIZE / 2));
    }
    return immediateOffensiveMoves(boardClass, stoneType)
        .orElseGet(() -> immediateDefensiveMoves(boardClass, stoneType)
            .orElseGet(() -> regularOffensiveMoves(boardClass, stoneType)
                .orElseGet(() -> regularDefensiveMoves(boardClass, stoneType)
                    .orElseGet(() -> getNeighboringMoves(boardClass)))));
  }
}
