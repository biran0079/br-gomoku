package ai.proofnumber;

import ai.candidatemoveselector.CandidateMovesSelector;
import ai.evaluator.Evaluator;
import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.Pattern;
import model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement proof number search algorithm.
 */
public class ProofNumberSearch<T extends Pattern> {

  private static final int MAX = Integer.MAX_VALUE;

  private final CandidateMovesSelector<T> candidateMovesSelector;
  private final Evaluator<T, Result> evaluator;

  public ProofNumberSearch(CandidateMovesSelector<T> candidateMovesSelector,
                    Evaluator<T, Result> evaluator) {
    this.candidateMovesSelector = candidateMovesSelector;
    this.evaluator = evaluator;
  }

  public Result search(BoardClass<T> boardClass, StoneType nextToMove) {
    Node root = new Node(null, boardClass, nextToMove);
    Node current = root;
    while (root.result == Result.UNKNOWN) {
      Node mostProvingNode = selectMostProovingNode(current);
      developNode(mostProvingNode);
      current = updateAncestors(mostProvingNode);
      if (root.proof == 0) {
        root.result = Result.TRUE;
      } else if (root.disproof == 0) {
        root.result = Result.FALSE;
      }
    }
    System.err.printf("%d nodes created\n", root.count());
    return root.result;
  }

  private boolean setProofDisproofNumber(Node node) {
    int proof = 0, disproof = 0;
    if (!node.children.isEmpty()) {
      switch (node.type) {
        case AND: {
          proof = 0;
          disproof = MAX;
          for (Node child : node.children) {
            proof += child.proof;
            disproof = Math.min(disproof, child.disproof);
          }
          break;
        }
        case OR: {
          proof = MAX;
          disproof = 0;
          for (Node child : node.children) {
            disproof += child.disproof;
            proof = Math.min(proof, child.proof);
          }
          break;
        }
      }
    } else {
      switch (node.result) {
        case TRUE:
          proof = 0;
          disproof = MAX;
          break;
        case FALSE:
          proof = MAX;
          disproof = 0;
          break;
        case UNKNOWN:
          proof = disproof = 1 + node.level / 2;
          break;
      }
    }
    if (proof == node.proof && disproof == node.disproof) {
      return false;
    }
    node.proof = proof;
    node.disproof = disproof;
    if ((proof == 0 || disproof == 0) && node.level <= 2) {
      System.err.println("Node at levelt " + node.level + (proof == 0 ? " proved" : " disproved"));
    }
    return true;
  }

  private Node updateAncestors(Node node) {
    while (true) {
      if (!setProofDisproofNumber(node)) {
        return node;
      }
      if (node.parent == null) {
        return node;
      }
      node = node.parent;
    }
  }

  private void developNode(Node node) {
    BoardClass<T> boardClass = node.boardClass;
    for (Position p : candidateMovesSelector.getCandidateMoves(
        boardClass, node.nextToMove)) {
      Node child = new Node(node,
          boardClass.withPositionSet(p, node.nextToMove),
          node.nextToMove.getOpponent());
      setProofDisproofNumber(child);
      node.children.add(child);
    }

  }

  private Node selectMostProovingNode(Node node) {
    while (!node.children.isEmpty()) {
      switch (node.type) {
        case OR:
          for (Node child : node.children) {
            if (child.proof == node.proof) {
              node = child;
              break;
            }
          }
          break;
        case AND:
          for (Node child : node.children) {
            if (child.disproof == node.disproof) {
              node = child;
              break;
            }
          }
          break;
      }
    }
    return node;
  }

  private enum Type {
    AND,
    OR
  }

  class Node {

    private int proof;
    private int disproof;
    private Result result;

    private final List<Node> children = new ArrayList<>();
    private final Node parent;
    private final Type type;
    private final BoardClass<T> boardClass;
    private final StoneType nextToMove;
    private final int level;

    Node(Node parent, BoardClass<T> boardClass, StoneType nextToMove) {
      this.parent = parent;
      this.boardClass = boardClass;
      this.nextToMove = nextToMove;
      this.type = nextToMove == StoneType.BLACK ? Type.OR : Type.AND;
      this.result = evaluator.eval(boardClass, nextToMove);
      this.level = parent == null ? 0 : parent.level + 1;
      this.proof = this.disproof = level;
    }

    public int count() {
      int res = 1;
      for (Node child : children) {
        res += child.count();
      }
      return res;
    }
  }
}
