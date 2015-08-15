package ai.minmax.transitiontable;

import ai.minmax.MinMaxNode;

import common.Transformable;
import common.boardclass.BoardClass;

/**
 * Transition table with hit/miss stats.
 */
public class TransitionTableWithStats<T extends Transformable<T>> implements TransitionTable<T> {

  private final TransitionTable<T> delegate;
  private final Stats stats;

  public TransitionTableWithStats(TransitionTable<T> delegate, Stats stats) {
    this.delegate = delegate;
    this.stats = stats;
  }

  @Override
  public T get(BoardClass boardClass) {
    T result = delegate.get(boardClass);
    if (result == null) {
      stats.incMiss();
    } else {
      stats.incHit();
    }
    return result;
  }

  @Override
  public void put(BoardClass boardClass, T node) {
    delegate.put(boardClass, node);
  }

  /**
   * Thread-safe stats.
   */
  public static class Stats {

    private final String name;

    private volatile int hit = 0;
    private volatile int miss = 0;

    public Stats(String name) {
      this.name = name;
    }

    public synchronized void incHit() {
      hit++;
    }

    public synchronized void incMiss() {
      miss++;
    }

    @Override
    public synchronized String toString() {
      return new StringBuilder(name)
          .append(" hit rate: ")
          .append(String.format("%.2f (%d/%d)", hit * 1.0 / (hit + miss + 1e-6), hit, (hit + miss)))
          .toString();
    }
  }
}
