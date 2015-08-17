package common.boardclass;

/**
 * Factories of BoardClass.
 */
public class BoardFactories {

  public static final BoardClass.Factory<BoardClassWithMatchingPatterns>
      BOARD_CLASS_WITH_MATCHING_PATTERNS_FACTORY = new BoardClassWithMatchingPatterns.Factory();
}
