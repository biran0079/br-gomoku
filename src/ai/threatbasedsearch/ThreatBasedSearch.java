package ai.threatbasedsearch;

import ai.minmax.transitiontable.TransitionSet;
import ai.minmax.transitiontable.TransitionSetImpl;
import com.google.common.collect.Iterables;
import common.boardclass.BoardClass;
import common.pattern.PatternType;
import common.StoneType;
import common.boardclass.threatbased.BoardClassWithMatchingThreats;
import common.pattern.Threat;
import model.Position;

/**
 * Threat based search.
 */
public class ThreatBasedSearch {

  public boolean winningPathExists(
      BoardClass<Threat> boardClass,
      StoneType attacker) {
    TransitionSet transitionSet = new TransitionSetImpl();
    return search(boardClass, attacker, transitionSet);
  }

  private boolean search(BoardClass<Threat> boardClass,
                         StoneType attacker,
                         TransitionSet transitionSet) {
    if (transitionSet.contains(boardClass)) {
      return false;
    }
    if (boardClass.matchesAny(attacker, PatternType.FIVE)
        || boardClass.matchesAny(attacker, PatternType.STRAIT_FOUR)) {
      return true;
    }
    for (Threat threat :
        Iterables.concat(
            boardClass.getMatchingPatterns(attacker, PatternType.THREE),
            boardClass.getMatchingPatterns(attacker, PatternType.FOUR))) {
      if (search(applyThreat(boardClass, threat), attacker, transitionSet)) {
        return true;
      }
    }
    transitionSet.add(boardClass);
    return false;
  }

  private BoardClass<Threat> applyThreat(
      BoardClass<Threat> boardClass, Threat threat) {
    Position p = threat.getOffensiveMove();
    boardClass = boardClass.withPositionSet(
        p.getRowIndex(), p.getColumnIndex(), threat.getStoneType());
    StoneType opponent = threat.getStoneType().getOpponent();
    for (Position def : threat.getDefensiveMoves()) {
      boardClass = boardClass.withPositionSet(
          def.getRowIndex(), def.getColumnIndex(), opponent);
    }
    return boardClass;
  }
}
