package common.pattern;

import common.PatternType;
import common.StoneType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for Threats.
 */
public class ThreatsTest {

  private final Threats threats = new Threats();

  @Test
  public void testGet() throws Exception {
    int ct = 0;
    for (StoneType stoneType : new StoneType[] {StoneType.BLACK, StoneType.WHITE}) {
      for (PatternType patternType : PatternType.values()) {
        ct += threats.get(stoneType, patternType).size();
      }
    }
    assertEquals(47192, ct);
  }
}