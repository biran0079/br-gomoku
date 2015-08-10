package model;

import common.StoneType;

import javax.inject.Inject;
import java.util.Stack;

/**
 * Implementation of history.
 */
public class HistoryImpl implements History {

  private final Stack<HistoryEntry> historyEntries = new Stack<>();

  @Inject
  HistoryImpl() {}

  @Override
  public void recordMove(Position position, StoneType stoneType) {
    historyEntries.add(new HistoryEntry(position, stoneType));
  }

  @Override
  public HistoryEntry getLastMove() {
    return historyEntries.pop();
  }

  @Override
  public boolean hasMore() {
    return !historyEntries.isEmpty();
  }

  @Override
  public void clear() {
    historyEntries.clear();
  }
}
