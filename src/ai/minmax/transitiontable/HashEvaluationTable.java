package ai.minmax.transitiontable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.math.DoubleMath;

import common.PositionTransformer;
import common.Transformable;
import common.boardclass.BitBoard;
import common.boardclass.BoardClass;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Evaluate hash function collision rate.
 */
public class HashEvaluationTable<T extends Transformable<T>> extends TransitionTableImpl<T> {

  private final Multimap<Integer, BitBoard> hashBucket = ArrayListMultimap.create();

  @Override
  public void put(BoardClass boardClass, T node) {
    for (PositionTransformer transformer : IDENTICAL_TRANSFORMERS) {
      BitBoard bitBoard = boardClass.getBoard(transformer);
      if (!cache.containsKey(bitBoard)) {
        cache.put(bitBoard, node.transform(transformer));
        hashBucket.put(bitBoard.hashCode(), bitBoard);
      }
    }
  }

  public void printStats() {
    List<Integer> collisions = hashBucket.keys().stream().map(h -> hashBucket.get(h).size())
        .collect(Collectors.toList());
    if (collisions.isEmpty()) {
      System.out.println("Empty transition table");
    } else {
      System.out.printf("Total: %d\nCollision: %.2f [%d, %d]\n",
          cache.size(),
          DoubleMath.mean(collisions),
          Collections.min(collisions),
          Collections.max(collisions));
    }
  }
}
