package ai.threatbasedsearch;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Threat based search.
 */
public class ThreatBasedSearch {

  public Position winningMove(
      BoardClass<Threat> boardClass,
      StoneType attacker) {
    List<Threat> threats = new ArrayList<>();
    if (search(boardClass, attacker, threats)) {
      threats = Lists.reverse(threats);
      Set<Position> related = new HashSet<>();
      for (Threat t : threats) {
        related.add(t.getOffensiveMove());
        related.addAll(t.getDefensiveMoves());
      }
      if (!globalDefend(boardClass, threats, attacker, related)) {
        return threats.get(0).getOffensiveMove();
      }
    }
    return null;
  }

  private boolean globalDefend(BoardClass<Threat> boardClass, List<Threat> threats,
                               StoneType attacker,
                               Set<Position> related) {
    StoneType defender = attacker.getOpponent();
    BoardClass<Threat> current = boardClass;
    for (Threat threat : threats.subList(0, threats.size() - 1)) {
      current = current.withPositionSet(threat.getOffensiveMove(), attacker);
      if (boardClass.matchesAny(defender, PatternType.FIVE)) {
        return true;
      }
      if (threat.getDefensiveMoves().size() >= 2) {
        if (defendWithFour(current, defender, related)) {
          return true;
        }
      }
      for (Position p : threat.getDefensiveMoves()) {
        current = current.withPositionSet(p, defender);
      }
    }
    return false;
  }

  private boolean defendWithFour(BoardClass<Threat> current, StoneType defender, Set<Position> related) {
    if (current.matchesAny(defender, PatternType.FIVE)) {
      return true;
    }
    for (Threat threat : Iterables.concat(
        current.getMatchingPatterns(defender, PatternType.STRAIT_FOUR),
        current.getMatchingPatterns(defender, PatternType.FOUR))) {
      if (related.contains(threat.getOffensiveMove())) {
        return true;
      }
      if (defendWithFour(applyThreat(current, threat), defender, related)) {
        return true;
      }
    }
    return false;
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
    Set<Threat> threats = corpus.get(threat.getOffensiveMove(), threat.getStoneType());
    return Sets.intersection(intersectWith, threats);
  }

  private boolean search(BoardClass<Threat> boardClass,
                         StoneType attacker,
                         List<Threat> result) {
    List<Threat> threats = getThreats(boardClass, attacker);
    for (Threat threat : boardClass.getMatchingPatterns(attacker, PatternType.FIVE)) {
      result.add(threat);
      return true;
    }
    for (int i = 0; i < threats.size(); i++) {
      Threat t = threats.get(i);
      Position p = t.getOffensiveMove();
      Set<Threat> candidates = boardClass.getCorpus().get(p, t.getStoneType());
      if (search(applyThreat(boardClass, t), 1,
          threats.subList(i + 1, threats.size()),
          candidates, attacker, result)) {
        result.add(t);
        return true;
      }
    }
    return false;
  }

  private boolean search(BoardClass<Threat> boardClass,
                         int n,
                         List<Threat> threats,
                         Set<Threat> candidate,
                         StoneType attacker,
                         List<Threat> result) {
    for (Threat threat : boardClass.getMatchingPatterns(attacker, PatternType.FIVE)) {
      result.add(threat);
      return true;
    }
    for (Threat t : boardClass.filterMatching(candidate)) {
      Position p = t.getOffensiveMove();
      Set<Threat> c = boardClass.getCorpus().get(p, t.getStoneType());
      if (search(applyThreat(boardClass, t), 1,
          getThreats(boardClass, attacker),
          c, attacker, result)) {
        result.add(t);
        return true;
      }
    }
    if (n < 4) {
      for (int i = 0; i < threats.size(); i++) {
        Threat t = threats.get(i);
        if (!t.matches(boardClass)) {
          continue;
        }
        Set<Threat> cand = intersectThreatsAt(t, boardClass.getCorpus(), candidate);
        if (cand.isEmpty()) {
          continue;
        }
        if (search(applyThreat(boardClass, t), n + 1,
            threats.subList(i + 1, threats.size()), cand, attacker, result)) {
          result.add(t);
          return true;
        }
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
