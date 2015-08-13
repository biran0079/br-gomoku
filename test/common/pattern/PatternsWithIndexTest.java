package common.pattern;

import static org.junit.Assert.*;

import common.StoneType;

import org.junit.Test;

public class PatternsWithIndexTest {

  private PatternsWithIndex patternsWithIndex = new PatternsWithIndex();

  @Test
  public void testGet() {
    patternsWithIndex.get(0, 0, StoneType.WHITE);

  }
}