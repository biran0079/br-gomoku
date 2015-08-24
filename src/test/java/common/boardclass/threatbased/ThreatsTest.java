package common.boardclass.threatbased;

import common.StoneType;
import common.pattern.PatternType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for Threats.
 */
public class ThreatsTest {

  private final Threats threats = new Threats();

  @Test
  public void testGet() throws Exception {
    int ct = 0;
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE}) {
      for (PatternType patternType :
          new PatternType[] {PatternType.THREE, PatternType.FOUR, PatternType.STRAIT_FOUR, PatternType.FIVE}) {
        ct += threats.get(stoneType, patternType).size();
      }
    }
    assertEquals(47192, ct);
  }
}