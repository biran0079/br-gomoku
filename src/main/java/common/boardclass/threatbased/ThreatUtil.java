package common.boardclass.threatbased;

import autovalue.shaded.com.google.common.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.Threat;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static common.pattern.PatternType.*;

/**
 * Utility methods for threats.
 */
public class ThreatUtil {

  public static Set<Threat> getThreatsSet(BoardClass<Threat> boardClass, StoneType attacker) {
    return ImmutableSet.<Threat>builder()
        .addAll(boardClass.getMatchingPatterns(attacker, FIVE))
        .addAll(boardClass.getMatchingPatterns(attacker, FOUR))
        .addAll(boardClass.getMatchingPatterns(attacker, STRAIT_FOUR))
        .addAll(boardClass.getMatchingPatterns(attacker, THREE))
        .build();
  }

  public static List<Threat> restrictedThreats(Set<Threat> applicableThreats, int maxThreatLevel) {
    List<Threat> threats = Lists.newArrayList(
        Iterables.filter(applicableThreats,
            (t) -> t.getPatternType().getThreatLevel() <= maxThreatLevel));
    List<Threat> result = Lists.newArrayList(threats);
    Collections.sort(result,
        (x, y) -> x.getPatternType().getThreatLevel() - y.getPatternType().getThreatLevel());
    for (int i = 0; i < threats.size(); i++) {
      for (int j = 0; j < threats.size(); j++) {
        if (i == j) {
          continue;
        }
        if (threats.get(j).covers(threats.get(i))) {
          result.remove(threats.get(i));
          break;
        }
      }
    }
    return result;
  }
}
