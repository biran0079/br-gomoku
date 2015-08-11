package ai.minmax;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.math.DoubleMath;

import common.BitBoard;
import common.BoardClass;
import common.PositionTransformer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Evaluate hash function collision rate.
 */
public class HashEvaluationTable extends TransitionTableImpl {

  private final Multimap<Integer, BitBoard> hashBucket = ArrayListMultimap.create();

  @Override
  public void put(BoardClass boardClass, MinMaxNode node) {
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
