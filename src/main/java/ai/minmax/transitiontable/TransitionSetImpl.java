package ai.minmax.transitiontable;


import common.PositionTransformer;
import common.Transformable;
import common.boardclass.BoardClass;

class TransitionSetImpl implements TransitionSet {

  private final TransitionTable<DummyValue> transitionTable;

  TransitionSetImpl(TransitionTable.Factory<DummyValue> factory) {
    transitionTable = factory.create();
  }

  @Override
  public boolean contains(BoardClass<?> boardClass) {
    return transitionTable.get(boardClass) != null;
  }

  @Override
  public void add(BoardClass<?> boardClass) {
    transitionTable.put(boardClass, DummyValue.INSTANCE);
  }


  static class DummyValue implements Transformable<DummyValue> {

    private static final DummyValue INSTANCE = new DummyValue();

    @Override
    public DummyValue transform(PositionTransformer transformer) {
      return this;
    }
  }
}
