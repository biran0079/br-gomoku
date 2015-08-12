package ai.experiment;

import common.StoneType;

import ai.competition.BoardClassSamples;
import ai.minmax.HashEvaluationTable;
import ai.minmax.MinMaxSearch;

public class HashExperimentMain {

  public static void main(String[] args) {
    BoardClassSamples.INITIAL_10.stream()
        .forEach(boardClass -> {
          HashEvaluationTable table = new HashEvaluationTable();
          MinMaxSearch.newBuilder()
              .withName("d6")
              .withTransitionTableFactory(() -> table)
              .withMaxDepth(6)
              .build()
              .nextMove(boardClass, StoneType.BLACK);
          table.printStats();
        });
  }
}