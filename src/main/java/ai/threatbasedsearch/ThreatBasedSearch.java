package ai.threatbasedsearch;

import static common.pattern.PatternType.*;

import autovalue.shaded.com.google.common.common.collect.ImmutableSet;
import com.google.auto.value.AutoValue;
import com.google.common.collect.*;
import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.Threat;
import model.Position;

import java.util.*;
import java.util.logging.Logger;

/**
 * Threat based search.
 */
public class ThreatBasedSearch {

  private static final Logger logger = Logger.getLogger(ThreatBasedSearch.class.getName());

  public Position winningMove(
      BoardClass<Threat> boardClass,
      StoneType attacker) {
    Context context = Context.builder()
        .attacker(attacker)
        .maxThreatLevel(2)
        .defensiveCheck(true)
        .relatedMoves(ImmutableSet.<Position>of())
        .root(Node.createRootNode(boardClass))
        .build();
    Node goal = DBS(context);
    if (goal != null) {
        return findAllParentsThreats(goal).get(0).getOffensiveMove();
    }
    return null;
  }

  private Set<Threat> getThreatsSet(BoardClass<Threat> boardClass, StoneType attacker) {
    return ImmutableSet.<Threat>builder()
        .addAll(boardClass.getMatchingPatterns(attacker, FIVE))
        .addAll(boardClass.getMatchingPatterns(attacker, FOUR))
        .addAll(boardClass.getMatchingPatterns(attacker, STRAIT_FOUR))
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

  private Node DBS(Context context) {
    try {
      do {
        context.level++;
        Node goal = addDependencyStage(context.root(), context);
        if (goal != null) {
          return goal;
        }
      } while (addCombinationStage(context));
    } catch (TooManyRefutationException e) {
      // logger.info("Too many refutations for:\n" + context.root().getBoard());
    }
    return null;
  }

  private int getMinThreatLevel(BoardClass<Threat> boardClass, StoneType stoneType) {
    if (boardClass.matchesAny(stoneType, FIVE)) {
      return 0;
    }
    if (boardClass.matchesAny(stoneType, FOUR)) {
      return 1;
    }
    if (boardClass.matchesAny(stoneType, THREE)) {
      return 2;
    }
    return 3;
  }

  private boolean anyGlobalDefend(Context context, Node goal) {
    if (!context.defensiveCheck()) {
      return false;
    }
    BoardClass<Threat> current = context.root().getBoard();
    List<BoardClass<Threat>> intermediate = new ArrayList<>();
    Set<Position> relatedMoves = goal.getRelatedMoves();
    for (Threat threat : findAllParentsThreats(goal)) {
      current = current.withPositionSet(threat.getOffensiveMove(), threat.getStoneType());
      for (int i = 0; i < intermediate.size(); i++) {
        intermediate.set(i, intermediate.get(i).withPositionSet(
            threat.getOffensiveMove(), threat.getStoneType()));
      }
      Context newContext = Context.builder()
          .root(Node.createRootNode(current))
          .attacker(context.defender())
          .maxThreatLevel(getMinThreatLevel(current, context.attacker()))
          .relatedMoves(relatedMoves)
          .defensiveCheck(false)
          .build();
      if (DBS(newContext) != null) {
        context.threatSequenceRefuted();
        return true;
      }
      findAllDependencyBoards(newContext.root(), intermediate);
      for (Position def : threat.getDefensiveMoves()) {
        current = current.withPositionSet(def, context.defender());
        for (int i = 0; i < intermediate.size(); i++) {
          intermediate.set(i, intermediate.get(i).withPositionSet(def, context.defender()));
          if (intermediate.get(i).matchesAny(context.defender(), FIVE)) {
            context.threatSequenceRefuted();
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

  private boolean addCombinationStage(Context context) {
    boolean updated = false;
    List<DependencyNode> dependencyNode = findAllDependencyNodesByReverseLevel(context.root());
    for (int i = 0; i < dependencyNode.size(); i++) {
      DependencyNode nodeI = dependencyNode.get(i);
      if (nodeI.getLevel() < context.level) {
        break;
      }
      BoardClass<Threat> boardI = nodeI.getBoard();
      ImmutableSet<Position> relatedI = nodeI.getRelatedMoves();
      Set<Threat> candidateI = nodeI.getCandidateThreats();
      updated |= addCombinationInternal(1, dependencyNode.subList(i + 1, dependencyNode.size()),
          boardI, candidateI, relatedI, context.level, ImmutableList.of(nodeI));
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

  private Node addDependencyStage(Node node, Context context) {
    if ((node instanceof RootNode || node instanceof CombinationNode)
        && context.level == node.getLevel() + 1) {
      return addDependentChildren(node, context);
    }
    for (Node child : node.getChildren()) {
      Node goal = addDependencyStage(child, context);
      if (goal != null) {
        return goal;
      }
    }
    return null;
  }

  private Node addDependentChildren(Node node, Context context) {
    if (node.getBoard().matchesAny(context.attacker(), GOAL)) {
      if (anyGlobalDefend(context, node)) {
        return null;
      }
      return node;
    }
    Queue<Node> queue = new LinkedList<>();
    queue.add(node);
    while (!queue.isEmpty()) {
      node = queue.poll();
      Set<Threat> applicableThreats;
      if (node instanceof RootNode) {
        applicableThreats = getThreatsSet(node.getBoard(), context.attacker());
      } else {
        applicableThreats = node.getBoard().filterMatching(
            ((NodeWithCandidates) node).getCandidateThreats());
      }
      for (Threat threat : restrictedThreats(applicableThreats)) {
        if (threat.getPatternType().getThreatLevel() > context.maxThreatLevel()) {
          continue;
        }
        Node child = Node.createDependencyNode(threat, node, context.level);
        node.getChildren().add(child);
        BoardClass<Threat> board = child.getBoard();
        if (board.matchesAny(context.attacker(), GOAL)
            || context.relatedMoves().contains(threat.getOffensiveMove())) {
          if (anyGlobalDefend(context, child)) {
            continue;
          }
          return child;
        }
        if (!board.matchesAny(context.attacker(), FIVE)) {
          if (board.matchesAny(context.defender(), FIVE)) {
            continue;
          }
          if (!board.matchesAny(context.attacker(), FOUR)
              && board.matchesAny(context.defender(), STRAIT_FOUR)) {
            continue;
          }
        }
        queue.add(child);
      }
    }
    return null;
  }

  private List<Threat> restrictedThreats(Set<Threat> applicableThreats) {
    List<Threat> a = Lists.newArrayList(applicableThreats);
    List<Threat> result = Lists.newArrayList(applicableThreats);
    Collections.sort(result, (x, y) -> x.getPatternType().getThreatLevel() - y.getPatternType().getThreatLevel());
    for (int i = 0; i < a.size(); i++) {
      for (int j = 0; j < a.size(); j++) {
        if (i == j) {
          continue;
        }
        if (a.get(j).covers(a.get(i))) {
          result.remove(a.get(i));
          break;
        }
      }
    }
    return result;
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
  static abstract class Context {

    private int refutedCount = 0;
    private int level = 0;

    abstract Set<Position> relatedMoves();
    abstract boolean defensiveCheck();
    abstract int maxThreatLevel();
    abstract StoneType attacker();
    abstract Node root();

    void threatSequenceRefuted() {
      refutedCount++;
      if (refutedCount >= 20) {
        throw new TooManyRefutationException();
      }
    }

    StoneType defender() {
      return attacker().getOpponent();
    }

    static Builder builder() {
      return new AutoValue_ThreatBasedSearch_Context.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {

      abstract Builder relatedMoves(Set<Position> v);
      abstract Builder defensiveCheck(boolean v);
      abstract Builder maxThreatLevel(int v);
      abstract Builder attacker(StoneType v);
      abstract Builder root(Node root);

      abstract Context build();
    }
  }

  private static class TooManyRefutationException extends RuntimeException {}
}
