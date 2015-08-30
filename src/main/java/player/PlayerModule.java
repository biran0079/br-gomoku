package player;

import ai.AI;
import ai.minmax.MinMaxSearch;
import ai.minmax.transitiontable.SmartCompleteTransitionTable;
import ai.threatbasedsearch.CompeleteThreatSearchAI;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

/**
 * Module for Players.
 */
public class PlayerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(AI.class).toInstance(
        new CompeleteThreatSearchAI(
            MinMaxSearch.defaultBuilderForThreat()
                .withTransitionTableFactory(SmartCompleteTransitionTable::new)
                .withAlgorithm(MinMaxSearch.Algorithm.MINMAX)
                .withMaxDepth(7)
                .useKillerHeuristic()
                .build(),
            5));

    install(new FactoryModuleBuilder()
        .implement(Player.class, Names.named("human"), HumanPlayer.class)
        .implement(Player.class, Names.named("ai"), AIPlayer.class)
        .implement(Player.class, Names.named("test"), TestModePlayer.class)
        .build(PlayerFactory.class));
  }
}
