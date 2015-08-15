package ai.minmax.transitiontable;

import ai.minmax.MinMaxNode;
import com.google.common.collect.Maps;

import common.Transformable;
import common.boardclass.BitBoard;
import common.boardclass.BoardClass;
import common.PositionTransformer;

import java.util.Map;

import static common.PositionTransformer.*;
import static common.PositionTransformer.CLOCK_270_M;

/**
 * Non-thread-safe transition table.
 */
public class TransitionTableImpl<T extends Transformable<T>> implements TransitionTable<T> {

  protected static final PositionTransformer[] IDENTICAL_TRANSFORMERS =
      new PositionTransformer[] {
          IDENTITY,
          IDENTITY_M,
          CLOCK_90,
          CLOCK_90_M,
          CLOCK_180,
          CLOCK_180_M,
          CLOCK_270,
          CLOCK_270_M,
      };

  protected final Map<BitBoard, T> cache = Maps.newHashMap();

  @Override
  public T get(BoardClass boardClass) {
    return cache.get(boardClass.getBoard(PositionTransformer.IDENTITY));
  }

  @Override
  public void put(BoardClass boardClass, T node) {
    for (PositionTransformer transformer : IDENTICAL_TRANSFORMERS) {
      BitBoard bitBoard = boardClass.getBoard(transformer);
      if (!cache.containsKey(bitBoard)) {
        cache.put(bitBoard, node.transform(transformer));
      }
    }
  }
}
