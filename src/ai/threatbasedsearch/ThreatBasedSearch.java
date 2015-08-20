package ai.threatbasedsearch;

import ai.minmax.transitiontable.TransitionSet;
import ai.minmax.transitiontable.TransitionSetImpl;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.Pattern;
import common.pattern.PatternType;
import common.pattern.Threat;
import model.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Threat based search.
 */
class ThreatBasedSearch {

  public boolean winningPathExists(
      BoardClass<Threat> boardClass,
      StoneType attacker) {
    return search(boardClass, attacker);
  }

  private List<Threat> getThreats(BoardClass<Threat> boardClass,
                                  StoneType attacker) {
    return Lists.newArrayList(Iterables.concat(
        boardClass.getMatchingPatterns(attacker, PatternType.STRAIT_FOUR),
        boardClass.getMatchingPatterns(attacker, PatternType.THREE),
        boardClass.getMatchingPatterns(attacker, PatternType.FOUR)));
  }

  private Set<Threat> intersectThreatsAt(Threat threat, Pattern.Corpus<Threat> corpus,
                                         Set<Threat> intersectWith) {
    Position p = threat.getOffensiveMove();
    Set<Threat> threats = corpus.get(p.getRowIndex(), p.getColumnIndex(), threat.getStoneType());
    return Sets.intersection(intersectWith, threats);
  }

  private boolean search(BoardClass<Threat> boardClass,
                         StoneType attacker) {
    List<Threat> threats = getThreats(boardClass, attacker);
    if (boardClass.matchesAny(attacker, PatternType.FIVE)) {
      return true;
    }
    for (int i = 0; i < threats.size(); i++) {
      Threat t = threats.get(i);
      Position p = t.getOffensiveMove();
      Set<Threat> c = boardClass.getCorpus().get(p.getRowIndex(), p.getColumnIndex(), t.getStoneType());
      if (search(applyThreat(boardClass, t), 1,
          threats.subList(i + 1, threats.size()),
          c, attacker)) {
        System.out.println(boardClass);
        return true;
      }
    }
    return false;
  }

  private boolean search(BoardClass<Threat> boardClass,
                         int n,
                         List<Threat> threats,
                         Set<Threat> candidate,
                         StoneType attacker) {
    if (boardClass.matchesAny(attacker, PatternType.FIVE)) {
      System.out.println(boardClass);
      return true;
    }
    for (Threat t : boardClass.filterMatching(candidate)) {
      Position p = t.getOffensiveMove();
      Set<Threat> c = boardClass.getCorpus().get(p.getRowIndex(), p.getColumnIndex(), t.getStoneType());
      if (search(applyThreat(boardClass, t), 1,
          getThreats(boardClass, attacker),
          c, attacker)) {
        System.out.println(boardClass);
        return true;
      }
    }
    for (int i = 0; i < threats.size(); i++) {
      Threat t = threats.get(i);
      if (!t.matches(boardClass)) {
        continue;
      }
      Set<Threat> cand = intersectThreatsAt(t, boardClass.getCorpus(), candidate);
      if (cand.isEmpty()) {
        continue;
      }
      if (n < 4 && search(applyThreat(boardClass, t), n + 1,
          threats.subList(i + 1, threats.size()), cand, attacker)) {
        System.out.println(boardClass);
        return true;
      }
    }
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
