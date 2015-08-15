package model;

import common.StoneType;

import java.util.Stack;

import javax.inject.Inject;

/**
 * Implementation of history.
 */
public class HistoryImpl implements History {

  private final Stack<HistoryEntry> historyEntries = new Stack<>();

  @Inject
  HistoryImpl() {}

  @Override
  public void recordMove(GameBoard preState, Position lastMove, StoneType stoneType) {
    historyEntries.add(new HistoryEntry(preState, lastMove, stoneType));
  }

  @Override
  public HistoryEntry popLastEntry() {
    return historyEntries.pop();
  }

  @Override
  public int size() {
    return historyEntries.size();
  }

  @Override
  public void clear() {
    historyEntries.clear();
  }
}
