package ai.minmax.transitiontable;

/**
 * Factory methods for transition set.
 */
public class TransitionSets {

  public static TransitionSet createCompleteTransitionSet() {
    return new TransitionSetImpl(CompleteTransitionTable::new);
  }

  public static TransitionSet createSimpleTransitionSet() {
    return new TransitionSetImpl(SimpleTransitionTable::new);
  }
}
