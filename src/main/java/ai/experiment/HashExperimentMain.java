package ai.experiment;

import ai.minmax.MinMaxSearch;
import ai.minmax.transitiontable.HashEvaluationTable;
import common.StoneType;
import common.boardclass.testing.BoardClassSamples;

class HashExperimentMain {

  public static void main(String[] args) {
    BoardClassSamples.INITIAL_10.stream()
        .forEach(boardClass -> {
          HashEvaluationTable table = new HashEvaluationTable();
          MinMaxSearch.defaultBuilderForPattern()
              .withName("d6")
              .withTransitionTableFactory(() -> table)
              .withMaxDepth(6)
              .build()
              .nextMove(boardClass, StoneType.BLACK);
          table.printStats();
        });
  }
}