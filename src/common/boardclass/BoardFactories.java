package common.boardclass;

/**
 * Factories of BoardClass.
 */
public class BoardFactories {

  public static final BoardClass.Factory<BoardClassWithMatchingPatterns> PRE_COMPUTE_MATCHING_FACTORY =
      new BoardClassWithMatchingPatterns.Factory();
}
