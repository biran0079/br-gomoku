package ai.proofnumber;

import ai.candidatemoveselector.CandidateMovesSelector;
import ai.evaluator.Evaluator;
import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.Pattern;
import model.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implement proof number search algorithm.
 */
public class ProofNumberSearch<T extends Pattern> {

  private static final int MAX = Integer.MAX_VALUE;

  private int evalCount;
  private final CandidateMovesSelector<T> candidateMovesSelector;
  private final Evaluator<T, Result> evaluator;

  public ProofNumberSearch(CandidateMovesSelector<T> candidateMovesSelector,
                    Evaluator<T, Result> evaluator) {
    this.candidateMovesSelector = candidateMovesSelector;
    this.evaluator = evaluator;
  }

  public Result search(BoardClass<T> boardClass, StoneType nextToMove) {
    Node<T> root = new Node<>(null, boardClass, nextToMove, evaluator.eval(boardClass, nextToMove));
    Node<T> current = root;
    evalCount = 1;
    while (root.result == Result.UNKNOWN) {
      Node<T> mostProvingNode = selectMostProovingNode(current);
      developNode(mostProvingNode);
      current = updateAncestors(mostProvingNode);
      if (root.proof == 0) {
        root.result = Result.TRUE;
      } else if (root.disproof == 0) {
        root.result = Result.FALSE;
      }
    }
    System.err.printf("%d nodes created\n", evalCount);
    return root.result;
  }

  private boolean setProofDisproofNumber(Node<T> node) {
    int proof = 0, disproof = 0;
    if (!node.children.isEmpty()) {
      switch (node.type) {
        case AND: {
          proof = 0;
          disproof = MAX;
          for (Iterator<Node<T>> it = node.children.iterator(); it.hasNext();) {
            Node<T> child = it.next();
            if (child.proof == 0) {
              it.remove();
            } else {
              proof += child.proof;
              disproof = Math.min(disproof, child.disproof);
            }
          }
          break;
        }
        case OR: {
          disproof = 0;
          proof = MAX;
          for (Iterator<Node<T>> it = node.children.iterator(); it.hasNext();) {
            Node<T> child = it.next();
            if (child.disproof == 0) {
              it.remove();
            } else {
              disproof += child.disproof;
              proof = Math.min(proof, child.proof);
            }
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
    if ((proof == 0 || disproof == 0)) {
      System.err.println("Node at level " + node.level + (proof == 0 ? " proved" : " disproved"));
    }
    return true;
  }

  private Node<T> updateAncestors(Node<T> node) {
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

  private void developNode(Node<T> node) {
    BoardClass<T> boardClass = node.boardClass;
    for (Position p : candidateMovesSelector.getCandidateMoves(
        boardClass, node.nextToMove)) {
      BoardClass<T> newBoardClass = boardClass.withPositionSet(p, node.nextToMove);
      StoneType newNext = node.nextToMove.getOpponent();
      Result result = evaluator.eval(newBoardClass, newNext);
      evalCount++;
      Node<T> child = new Node<>(node, newBoardClass, newNext, result);
      setProofDisproofNumber(child);
      node.children.add(child);
    }

  }

  private Node<T> selectMostProovingNode(Node<T> node) {
    while (!node.children.isEmpty()) {
      switch (node.type) {
        case OR:
          for (Node<T> child : node.children) {
            if (child.proof == node.proof) {
              node = child;
              break;
            }
          }
          break;
        case AND:
          for (Node<T> child : node.children) {
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

  static class Node<T extends Pattern> {

    private int proof;
    private int disproof;
    private Result result;

    private final List<Node<T>> children = new ArrayList<>();
    private final Node<T> parent;
    private final Type type;
    private final BoardClass<T> boardClass;
    private final StoneType nextToMove;
    private final int level;

    Node(Node<T> parent, BoardClass<T> boardClass, StoneType nextToMove, Result result) {
      this.parent = parent;
      this.boardClass = boardClass;
      this.nextToMove = nextToMove;
      this.type = nextToMove == StoneType.BLACK ? Type.OR : Type.AND;
      this.result = result;
      this.level = parent == null ? 0 : parent.level + 1;
      this.proof = this.disproof = level;
    }

    @Override
    public String toString() {
      return result.toString();
    }
  }
}
