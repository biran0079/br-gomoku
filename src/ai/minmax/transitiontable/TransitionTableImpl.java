package ai.minmax.transitiontable;

import ai.minmax.MinMaxNode;
import com.google.common.collect.Maps;
import common.boardclass.BitBoard;
import common.boardclass.BoardClass;
import common.PositionTransformer;

import java.util.Map;

import static common.PositionTransformer.*;
import static common.PositionTransformer.CLOCK_270_M;

/**
 * Non-thread-safe transition table.
 */
public class TransitionTableImpl implements TransitionTable {

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

  protected final Map<BitBoard, MinMaxNode> cache = Maps.newHashMap();

  @Override
  public MinMaxNode get(BoardClass boardClass) {
    return cache.get(boardClass.getBoard(PositionTransformer.IDENTITY));
  }

  @Override
  public void put(BoardClass boardClass, MinMaxNode node) {
    for (PositionTransformer transformer : IDENTICAL_TRANSFORMERS) {
      BitBoard bitBoard = boardClass.getBoard(transformer);
      if (!cache.containsKey(bitBoard)) {
        cache.put(bitBoard, node.transform(transformer));
      }
    }
  }
}
