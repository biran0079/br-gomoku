package ai.threatbasedsearch;

import static common.pattern.PatternType.*;

import autovalue.shaded.com.google.common.common.collect.ImmutableSet;
import com.google.auto.value.AutoValue;
import com.google.common.collect.*;
import common.Constants;
import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.threatbased.ThreatUtil;
import common.pattern.Threat;
import model.Position;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Threat based search.
 */
public class ThreatBasedSearch {

  private static final TooManyRefutation TOO_MANY_REFUTATION = new TooManyRefutation();
  private static final GoalFound GOAL_FOUND = new GoalFound();
  private static final HitRelatedPosition HIT_RELATED_POSITION = new HitRelatedPosition();
  private final DatabaseManager databaseManager;

  public ThreatBasedSearch(DatabaseManager databaseManager) {
    this.databaseManager = databaseManager;
  }

  public ThreatBasedSearch() {
    this(null);
  }

  public Position winningMove(
      BoardClass<Threat> boardClass,
      StoneType attacker) {
    Context context = search(boardClass, attacker);
    if (context.goal != null) {
      Node goal = context.goal;
      return findAllParentsThreats(goal).get(0).getOffensiveMove();
    }
    return null;
  }

  public Node threatBasedTree(
      BoardClass<Threat> boardClass,
      StoneType attacker) {
    return search(boardClass, attacker).root();
  }

  public Context search(
      BoardClass<Threat> boardClass,
      StoneType attacker) {
    Context context = Context.builder()
        .attacker(attacker)
        .maxThreatLevel(2)
        .defensiveCheck(true)
        .relatedMoves(ImmutableSet.<Position>of())
        .root(Node.createRootNode(boardClass))
        .build();
    DBS(context);
    if (databaseManager != null && context.goal != null) {
      try {
        databaseManager.saveTreeIfNotExist(context.root());
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return context;
  }

  public Set<Position> findImplicitThreats(
      BoardClass<Threat> boardClass,
      StoneType attacker) {
    Context context = search(boardClass, attacker);
    if (context.goal != null) {
      Set<Position> result = new HashSet<>();
      result.addAll(context.goal.getRelatedMoves());
      for (int i = 0; i < Constants.BOARD_SIZE; i++) {
        for (int j = 0; j < Constants.BOARD_SIZE; j++) {
          Position p = Position.of(i, j);
          Context newContext = context.toBuilder()
              .root(Node.createRootNode(boardClass.withPositionSet(p, context.defender())))
              .build();
          if (!result.contains(p)
              && boardClass.get(p) == StoneType.NOTHING
              && anyGlobalDefend(newContext, context.goal)){
            result.add(p);
          }
        }
      }
      return result;
    }
    return Collections.emptySet();
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

  private void DBS(Context context) {
    try {
      do {
        context.level++;
        addDependencyStage(context.root(), context);
      } while (addCombinationStage(context));
    } catch (TooManyRefutation e) {
      // logger.info("Too many refutations for:\n" + context.root().getBoard());
    } catch (GoalFound e) {
      // quick termination when goal is found
    }
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

  private boolean anyGlobalDefendInternal(Context context, Node goal) {
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
      try {
        DBS(newContext);
      } catch (HitRelatedPosition e) {
        return true;
      }
      if (newContext.goal != null) {
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

  private boolean anyGlobalDefend(Context context, Node goal) {
    if (!context.defensiveCheck()) {
      return false;
    }
    goal.isGoal = true;
    goal.isRefuted = anyGlobalDefendInternal(context, goal);
    return goal.isRefuted;
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

  private void addDependencyStage(Node node, Context context) {
    if ((node instanceof RootNode || node instanceof CombinationNode)
        && context.level == node.getLevel() + 1) {
      addDependentChildren(node, context);
    } else {
      for (Node child : node.getChildren()) {
        addDependencyStage(child, context);
      }
    }
  }

  private void addDependentChildren(Node node, Context context) {
    if (node.getBoard().matchesAny(context.attacker(), GOAL)) {
      if (!anyGlobalDefend(context, node)) {
        context.goal = node;
        throw GOAL_FOUND;
      }
      return;
    }
    Queue<Node> queue = new LinkedList<>();
    queue.add(node);
    while (!queue.isEmpty()) {
      node = queue.poll();
      Set<Threat> applicableThreats;
      if (node instanceof RootNode) {
        applicableThreats = ThreatUtil.getThreatsSet(node.getBoard(), context.attacker());
      } else {
        applicableThreats = node.getBoard().filterMatching(
            ((NodeWithCandidates) node).getCandidateThreats());
      }
      for (Threat threat : ThreatUtil.restrictedThreats(applicableThreats, context.maxThreatLevel())) {
        if (context.relatedMoves().contains(threat.getOffensiveMove())) {
          throw HIT_RELATED_POSITION;
        }
        Node child = Node.createDependencyNode(threat, node, context.level);
        node.getChildren().add(child);
        BoardClass<Threat> board = child.getBoard();
        if (board.matchesAny(context.attacker(), GOAL)) {
          if (!anyGlobalDefend(context, child)) {
            context.goal = child;
            throw GOAL_FOUND;
          }
          continue;
        }
        if (context.defensiveCheck()) {
          // don't do this optimization for global defense search.
          if (!board.matchesAny(context.attacker(), FIVE)) {
            if (board.matchesAny(context.defender(), FIVE)) {
              continue;
            }
            if (!board.matchesAny(context.attacker(), FOUR)
                && board.matchesAny(context.defender(), STRAIT_FOUR)) {
              continue;
            }
          }
        }
        queue.add(child);
      }
    }
  }

  abstract static class Node {

    private boolean isGoal = false;
    private boolean isRefuted = false;

    public boolean isGoal() {
      return isGoal;
    }

    public boolean isRefuted() {
      return isRefuted;
    }

    @Override
    public final int hashCode() {
      return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
      return super.equals(o);
    }

    abstract BoardClass<Threat> getBoard();

    abstract int getLevel();

    abstract List<Node> getChildren();

    abstract ImmutableSet<Position> getRelatedMoves();

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

  abstract static class NodeWithCandidates extends Node {

    abstract Set<Threat> getCandidateThreats();
  }

  @AutoValue
  abstract static class RootNode extends Node {

    @Override
    public final String toString() {
      return getBoard().toString();
    }
  }

  @AutoValue
  abstract static class DependencyNode extends NodeWithCandidates {

    abstract Threat getThreat();

    abstract Node getParent();

    @Override
    public final String toString() {
      return getBoard().toString();
    }
  }

  @AutoValue
  abstract static class CombinationNode extends NodeWithCandidates {

    abstract ImmutableList<DependencyNode> getParents();

    @Override
    public final String toString() {
      return getBoard().toString();
    }
  }

  @AutoValue
  static abstract class Context {

    private int refutedCount = 0;
    private int level = 0;
    private Node goal = null;

    Node getGoal() {
      return goal;
    }

    abstract Set<Position> relatedMoves();
    abstract boolean defensiveCheck();
    abstract int maxThreatLevel();
    abstract StoneType attacker();
    abstract Node root();

    abstract Builder toBuilder();

    void threatSequenceRefuted() {
      refutedCount++;
      if (refutedCount >= 20) {
        throw TOO_MANY_REFUTATION;
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

  private static class TooManyRefutation extends RuntimeException {
  }

  private static class GoalFound extends RuntimeException {
  }

  private static class HitRelatedPosition extends RuntimeException {
  }
}
