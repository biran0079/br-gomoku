package ai.threatbasedsearch;

import ai.minmax.transitiontable.SmartTransitionTable;
import ai.minmax.transitiontable.TransitionSet;
import ai.minmax.transitiontable.TransitionSetImpl;
import com.google.common.collect.Iterables;
import common.PositionTransformer;
import common.StoneType;
import common.boardclass.BitBoard;
import common.boardclass.BoardClass;
import common.pattern.PatternType;
import common.pattern.Threat;
import model.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Performs complete threat search with all moves.
 */
class CompleteThreatSearch {

  Position winningMove(BoardClass<Threat> boardClass, StoneType attacker, int depth) {
    return offend(boardClass, attacker, new HashMap<>(), depth);
  }

  private Position offend(BoardClass<Threat> boardClass,
                          StoneType attacker,
                          Map<BitBoard, Position> cache,
                          int depth) {
    BitBoard key = boardClass.getBoard(PositionTransformer.IDENTITY);
    if (cache.containsKey(key)) {
      return cache.get(key);
    }
    StoneType defender = attacker.getOpponent();
    for (Threat threat : boardClass.getMatchingPatterns(attacker, PatternType.FIVE)) {
      cache.put(key, threat.getOffensiveMove());
      return threat.getOffensiveMove();
    }
    if (depth == 0) {
      return null;
    }
    if (boardClass.matchesAny(defender, PatternType.FIVE)) {
      cache.put(key, null);
      return null;
    }
    for (Threat threat : boardClass.getMatchingPatterns(attacker, PatternType.STRAIT_FOUR)) {
      cache.put(key, threat.getOffensiveMove());
      return threat.getOffensiveMove();
    }
    if (boardClass.matchesAny(defender, PatternType.STRAIT_FOUR)) {
      cache.put(key, null);
      return null;
    }
    for (Threat threat : Iterables.concat(
        boardClass.getMatchingPatterns(attacker, PatternType.THREE),
        boardClass.getMatchingPatterns(attacker, PatternType.FOUR))) {
      boolean defended = false;
      Position offendMove = threat.getOffensiveMove();
      for (Position defendMove : threat.getDefensiveMoves()) {
        if (offend(
            boardClass.withPositionSet(offendMove, attacker)
                .withPositionSet(defendMove, defender),
            attacker,
            cache,
            depth - 1) == null) {
          defended = true;
          break;
        }
      }
      if (!defended) {
        cache.put(key, offendMove);
        return offendMove;
      }
    }
    cache.put(key, null);
    return null;
  }
}
