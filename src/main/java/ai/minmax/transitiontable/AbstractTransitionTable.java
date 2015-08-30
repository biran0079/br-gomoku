package ai.minmax.transitiontable;

import com.google.common.collect.Maps;
import common.PositionTransformer;
import common.Transformable;
import common.boardclass.BitBoard;
import common.boardclass.BoardClass;

import java.util.Map;

/**
 * Abstract implementation of transition table.
 */
abstract class AbstractTransitionTable<T extends Transformable<T>> implements TransitionTable<T> {

  final Map<BitBoard, T> cache = Maps.newHashMap();
  private final PositionTransformer[] transformers;

  AbstractTransitionTable(PositionTransformer[] transformers) {
    this.transformers = transformers;
  }

  @Override
  public T get(BoardClass<?> boardClass) {
    return cache.get(boardClass.getBoard(PositionTransformer.IDENTITY));
  }

  @Override
  public void put(BoardClass<?> boardClass, T node) {
    for (PositionTransformer transformer : transformers) {
      BitBoard bitBoard = boardClass.getBoard(transformer);
      if (!cache.containsKey(bitBoard)) {
        cache.put(bitBoard, node.transform(transformer));
      }
    }
  }
}
