package common.boardclass;

/**
 * Factories of BoardClass.
 */
public class BoardFactories {

  public static final BoardClass.Factory DEFAULT_FACTORY = new BoardClassImpl.Factory();
  public static final BoardClass.Factory PRE_COMPUTE_MATCHING_FACTORY =
      new BoardClassWithMatchingPatterns.Factory();
}
