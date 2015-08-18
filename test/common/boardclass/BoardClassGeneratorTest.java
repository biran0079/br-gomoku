package common.boardclass;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Iterables;

import org.junit.Test;

public class BoardClassGeneratorTest {

  private final BoardClassGenerator boardClassGenerator = new BoardClassGenerator();

  @Test
  public void testGenerateBoardWithStonesSize() throws Exception {
    assertEquals(1, Iterables.size(boardClassGenerator.generateBoardWithStones(0)));
    assertEquals(1, Iterables.size(boardClassGenerator.generateBoardWithStones(1)));
    assertEquals(2, Iterables.size(boardClassGenerator.generateBoardWithStones(2)));
    assertEquals(13, Iterables.size(boardClassGenerator.generateBoardWithStones(3)));
    for (BoardClass<?> boardClass : boardClassGenerator.generateBoardWithStones(4)) {
      assertEquals(4, boardClass.getStoneCount());
    }
  }
}