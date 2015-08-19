package ai.minmax.transitiontable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import common.PositionTransformer;
import common.Transformable;
import common.boardclass.BitBoard;
import common.boardclass.BoardClass;

import java.util.stream.Stream;

/**
 * Evaluate hash function collision rate.
 */
public class HashEvaluationTable<T extends Transformable<T>> extends TransitionTableImpl<T> {

  private final Multimap<Integer, BitBoard> hashBucket = ArrayListMultimap.create();

  @Override
  public void put(BoardClass<?> boardClass, T node) {
    for (PositionTransformer transformer : IDENTICAL_TRANSFORMERS) {
      BitBoard bitBoard = boardClass.getBoard(transformer);
      if (!cache.containsKey(bitBoard)) {
        cache.put(bitBoard, node.transform(transformer));
        hashBucket.put(bitBoard.hashCode(), bitBoard);
      }
    }
  }

  public void printStats() {
    Stream<Integer> collisions = hashBucket.keys().stream().map(h -> hashBucket.get(h).size());
    System.out.printf("Total: %d\nCollision: %s\n",
        cache.size(),
        collisions.mapToInt(v -> v).summaryStatistics());
  }
}
