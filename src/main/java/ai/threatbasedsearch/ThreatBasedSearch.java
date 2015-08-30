package ai.threatbasedsearch;

import static common.pattern.PatternType.*;

import autovalue.shaded.com.google.common.common.collect.ImmutableSet;
import com.google.auto.value.AutoValue;
import com.google.common.collect.*;
import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.Threat;
import model.Position;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Threat based search.
 */
public class ThreatBasedSearch {

  public Position winningMove(
      BoardClass<Threat> boardClass,
      StoneType attacker) {
    Node goal = DBS(boardClass, attacker, 2, ImmutableSet.of(), true);
    if (goal != null) {
        return findAllParentsThreats(goal).get(0).getOffensiveMove();
    }
    return null;
  }

  private Set<Threat> getThreatsSet(BoardClass<Threat> boardClass, StoneType attacker) {
    return ImmutableSet.<Threat>builder()
        .addAll(boardClass.getMatchingPatterns(attacker, FIVE))
        .addAll(boardClass.getMatchingPatterns(attacker, FOUR))
        .addAll(boardClass.getMatchingPatterns(attacker, THREE))
        .build();
  }

  private static BoardClass<Threat> applyThreat(
      BoardClass<Threat> boardClass, Threat threat) {
    Position p = threat.getOffensiveMove();
    boardClass = boardClass.withPositionSet(p, threat.getStoneType());
    StoneType opponent = threat.getStoneType().getOpponent();
    for (Position def : threat.getDefensiveMoves()) {
      boardClass = boardClass.withPositionSet(def, opponent);
    }
    return boardClass;
  }

  private Pair DBSInternal(BoardClass<Threat> boardClass,
                           StoneType attacker,
                           int maxThreatLevel,
                           Set<Position> relatedMoves,
                           boolean defensiveCheck) {
    RootNode root = Node.createRootNode(boardClass);
    int level = root.getLevel();
    do {
      level++;
      Node goal = addDependencyStage(root, attacker, level, maxThreatLevel, relatedMoves);
      if (goal != null) {
        if (defensiveCheck) {
          if (anyGlobalDefend(boardClass, attacker.getOpponent(),
              findAllParentsThreats(goal), goal.getRelatedMoves())) {
            goal = null;
          }
        }
        return Pair.create(root, goal);
      }
    } while (addCombinationStage(root, level));
    return Pair.create(root, null);
  }

  private Node DBS(BoardClass<Threat> boardClass,
                   StoneType attacker,
                   int maxThreatLevel,
                   Set<Position> relatedMoves,
                   boolean defensiveCheck) {
    return DBSInternal(boardClass, attacker, maxThreatLevel, relatedMoves, defensiveCheck).getGoal();
  }

  private boolean anyGlobalDefend(BoardClass<Threat> boardClass,
                                  StoneType defender,
                                  List<Threat> threats,
                                  Set<Position> relatedMoves) {
    BoardClass<Threat> current = boardClass;
    List<BoardClass<Threat>> intermediate = new ArrayList<>();
    for (Threat threat : threats) {
      current = current.withPositionSet(threat.getOffensiveMove(), threat.getStoneType());
      for (int i = 0; i < intermediate.size(); i++) {
        intermediate.set(i, intermediate.get(i).withPositionSet(
            threat.getOffensiveMove(), threat.getStoneType()));
      }
      Pair pair = DBSInternal(current, defender,
          threat.getPatternType().getThreatLevel() - 1,
          relatedMoves, false);
      if (pair.getGoal() != null) {
        return true;
      }
      findAllDependencyBoards(pair.getRoot(), intermediate);
      for (Position def : threat.getDefensiveMoves()) {
        current = current.withPositionSet(def, defender);
        for (int i = 0; i < intermediate.size(); i++) {
          intermediate.set(i, intermediate.get(i).withPositionSet(def, defender));
          if (intermediate.get(i).matchesAny(defender, FIVE)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private void findAllDependencyBoards(Node node, List<BoardClass<Threat>> dependencyBoards) {
    if (!(node instanceof RootNode)) {
      dependencyBoards.add(node.getBoard());
    }
    for (Node child : node.getChildren()) {
      findAllDependencyBoards(child, dependencyBoards);
    }
  }

  private static <T> boolean disjoint(Set<T> s1, Set<T> s2) {
    return Sets.intersection(s1, s2).isEmpty();
  }

  private boolean addCombinationStage(Node root, int level) {
    boolean updated = false;
    List<DependencyNode> dependencyNode = findAllDependencyNodesByReverseLevel(root);
    for (int i = 0; i < dependencyNode.size(); i++) {
      DependencyNode nodeI = dependencyNode.get(i);
      if (nodeI.getLevel() < level) {
        break;
      }
      BoardClass<Threat> boardI = nodeI.getBoard();
      ImmutableSet<Position> relatedI = nodeI.getRelatedMoves();
      Set<Threat> candidateI = nodeI.getCandidateThreats();
      updated |= addCombinationInternal(1, dependencyNode.subList(i + 1, dependencyNode.size()),
          boardI, candidateI, relatedI, level, ImmutableList.of(nodeI));
    }
    return updated;
  }

  private boolean addCombinationInternal(int n, List<DependencyNode> nodes,
                                         BoardClass<Threat> prevBoard,
                                         Set<Threat> prevCandidate,
                                         ImmutableSet<Position> prevRelativeMoves,
                                         int level,
                                         ImmutableList<DependencyNode> preNodes) {
    if (n >= 4) {
      return false;
    }
    boolean updated = false;
    for (int i = 0; i < nodes.size(); i++) {
      DependencyNode node = nodes.get(i);
      if (!disjoint(prevRelativeMoves, node.getRelatedMoves())) {
        continue;
      }
      Set<Threat> newCandidates = Sets.intersection(prevCandidate, node.getCandidateThreats());
      BoardClass<Threat> newBoard = applyThreats(prevBoard, findAllParentsThreats(node));
      Set<Threat> matchingThreats = newBoard.filterMatching(newCandidates);
      if (newCandidates.isEmpty()) {
        continue;
      }
      ImmutableSet<Position> newRelativeMoves = ImmutableSet.<Position>builder()
          .addAll(prevRelativeMoves)
          .addAll(node.getRelatedMoves())
          .build();
      ImmutableList<DependencyNode> parents = ImmutableList.<DependencyNode>builder()
          .add(node)
          .addAll(preNodes)
          .build();
      preNodes.get(0).getChildren().add(Node.createCombinationNode(
          newBoard, level, parents, matchingThreats, newRelativeMoves));
      updated = true;
      addCombinationInternal(n + 1, nodes.subList(i + 1, nodes.size()),
          newBoard, newCandidates, newRelativeMoves, level, parents);
    }
    return updated;
  }

  private BoardClass<Threat> applyThreats(BoardClass<Threat> boardClass, List<Threat> threats) {
    for (Threat threat : threats) {
      boardClass = applyThreat(boardClass, threat);
    }
    return boardClass;
  }

  private List<Threat> findAllParentsThreats(Node node) {
    List<Threat> result = new ArrayList<>();
    findAllParentsThreats(node, result);
    return result;
  }

  private void findAllParentsThreats(Node node, List<Threat> result) {
    if (node instanceof DependencyNode) {
      findAllParentsThreats(((DependencyNode) node).getParent(), result);
      result.add(((DependencyNode) node).getThreat());
    } else if (node instanceof CombinationNode) {
      for (Node parent : ((CombinationNode) node).getParents()) {
        findAllParentsThreats(parent, result);
      }
    }
  }

  private List<DependencyNode> findAllDependencyNodesByReverseLevel(Node root) {
    List<DependencyNode> result = new ArrayList<>();
    Queue<Node> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
      Node current = queue.poll();
      if (current instanceof DependencyNode) {
        result.add((DependencyNode) current);
      }
      queue.addAll(current.getChildren());
    }
    Collections.sort(result, (a, b) -> b.getLevel() - a.getLevel());
    return result;
  }

  private Node addDependencyStage(Node node, StoneType attacker, int level,
                                  int maxThreatLevel,
                                  Set<Position> relatedMoves) {
    if ((node instanceof RootNode || node instanceof CombinationNode)
        && level == node.getLevel() + 1) {
      return addDependentChildren(node, attacker, level, maxThreatLevel, relatedMoves);
    }
    for (Node child : node.getChildren()) {
      Node goal = addDependencyStage(child, attacker, level,
          maxThreatLevel, relatedMoves);
      if (goal != null) {
        return goal;
      }
    }
    return null;
  }

  private Node addDependentChildren(Node node, StoneType attacker, int level,
                                    int maxThreatLevel,
                                    Set<Position> relatedMoves) {
    Queue<Node> queue = new LinkedList<>();
    queue.add(node);
    while (!queue.isEmpty()) {
      node = queue.poll();
      Set<Threat> applicableThreats;
      if (node instanceof RootNode) {
        applicableThreats = getThreatsSet(node.getBoard(), attacker);
      } else {
        applicableThreats = node.getBoard().filterMatching(
            ((NodeWithCandidates) node).getCandidateThreats());
      }
      for (Threat threat : applicableThreats) {
        if (threat.getPatternType().getThreatLevel() > maxThreatLevel) {
          continue;
        }
        Node child = Node.createDependencyNode(threat, node, level);
        node.getChildren().add(child);
        if (child.getBoard().matchesAny(attacker, GOAL)
            || relatedMoves.contains(threat.getOffensiveMove())) {
          return child;
        }
        queue.add(child);
      }
    }
    return null;
  }

  interface Node {

    BoardClass<Threat> getBoard();

    int getLevel();

    List<Node> getChildren();

    ImmutableSet<Position> getRelatedMoves();

    static RootNode createRootNode(BoardClass<Threat> boardClass) {
      return new AutoValue_ThreatBasedSearch_RootNode(boardClass, 0, new ArrayList<>(), ImmutableSet.of());
    }

    static DependencyNode createDependencyNode(Threat threat,
                                               Node parent,
                                               int level) {
      BoardClass<Threat> boardClass = applyThreat(parent.getBoard(), threat);
      Set<Threat> candidateThreats = boardClass.getCorpus()
          .get(threat.getOffensiveMove(), threat.getStoneType());
      ImmutableSet<Position> relatedMoves = ImmutableSet.<Position>builder()
          .addAll(parent.getRelatedMoves())
          .add(threat.getOffensiveMove())
          .addAll(threat.getDefensiveMoves())
          .build();
      return new AutoValue_ThreatBasedSearch_DependencyNode(
          boardClass, level, new ArrayList<>(), relatedMoves, candidateThreats, threat, parent);
    }

    static CombinationNode createCombinationNode(BoardClass<Threat> boardClass,
                                                 int level,
                                                 ImmutableList<DependencyNode> parents,
                                                 Set<Threat> candidateThreats,
                                                 ImmutableSet<Position> relatedMoves) {
      return new AutoValue_ThreatBasedSearch_CombinationNode(
          boardClass, level, new ArrayList<>(), relatedMoves, candidateThreats, parents);
    }
  }

  interface NodeWithCandidates extends Node {

    Set<Threat> getCandidateThreats();
  }

  @AutoValue
  abstract static class RootNode implements Node {
  }

  @AutoValue
  abstract static class DependencyNode implements NodeWithCandidates {

    abstract Threat getThreat();

    abstract Node getParent();
  }

  @AutoValue
  abstract static class CombinationNode implements NodeWithCandidates {

    abstract ImmutableList<DependencyNode> getParents();
  }

  @AutoValue
  abstract static class Pair {

    static Pair create(Node root, Node goal) {
      return new AutoValue_ThreatBasedSearch_Pair(root, goal);
    }

    abstract Node getRoot();

    @Nullable
    abstract Node getGoal();
  }
}
