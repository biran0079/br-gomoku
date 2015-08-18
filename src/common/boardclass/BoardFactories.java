package common.boardclass;

import common.boardclass.basic.BoardClassWithMatchingPatterns;
import common.boardclass.threatbased.BoardClassWithMatchingThreats;
import common.pattern.Pattern;
import common.pattern.Threat;

/**
 * Factories of BoardClass.
 */
public class BoardFactories {

  public static final BoardClass.Factory<Pattern>
      BOARD_CLASS_WITH_MATCHING_PATTERNS_FACTORY = new BoardClassWithMatchingPatterns.Factory();

  public static final BoardClass.Factory<Threat>
      BOARD_CLASS_WITH_MATCHING_THREATS_FACTORY = new BoardClassWithMatchingThreats.Factory();
}
