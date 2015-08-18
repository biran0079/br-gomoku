package common.boardclass;

import common.boardclass.basic.FactoryForBoardWithPattern;
import common.boardclass.threatbased.FactoryForBoardWithThreat;
import common.pattern.Pattern;
import common.pattern.Threat;

/**
 * Factories of BoardClass.
 */
public class BoardFactories {

  public static final BoardClass.Factory<Pattern>
      BOARD_CLASS_WITH_MATCHING_PATTERNS_FACTORY = new FactoryForBoardWithPattern();

  public static final BoardClass.Factory<Threat>
      BOARD_CLASS_WITH_MATCHING_THREATS_FACTORY = new FactoryForBoardWithThreat();
}
