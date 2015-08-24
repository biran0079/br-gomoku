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
      FOR_PATTERN = new FactoryForBoardWithPattern();

  public static final BoardClass.Factory<Threat>
      FOR_THREAT = new FactoryForBoardWithThreat();
}
