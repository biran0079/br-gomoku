package ai.threatbasedsearch;

import ai.threatbasedsearch.ThreatBasedSearch.Node;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages database I/O.
 */
public class DatabaseManager {

  private static final String DB_PATH = "gomoku.db";
  private static final String IN_MEMORY = ":memory:";

  private static final String SCHEMA =
      "CREATE TABLE IF NOT EXISTS tree (root INTEGER PRIMARY KEY);" +
      "CREATE TABLE IF NOT EXISTS node (id INTEGER PRIMARY KEY, treeId INTEGER,  board TEXT," +
          " isGoal INTEGER, isRefuted INTEGER);" +
      "CREATE TABLE IF NOT EXISTS children (parentId INTEGER, childId INTEGER);" +
      "CREATE TABLE IF NOT EXISTS combination (childId INTEGER, parentId INTEGER);";

  private final String dbPath;
  private Connection connection = null;

  public static DatabaseManager fileDb() throws SQLException {
    return create(DB_PATH);
  }

  static DatabaseManager inMemoryDb() throws SQLException {
    return create(IN_MEMORY);
  }

  private static DatabaseManager create(String dbPath) throws SQLException {
    DatabaseManager databaseManager = new DatabaseManager(dbPath);
    databaseManager.initialize();
    return databaseManager;
  }

  private DatabaseManager(String dbPath) {
    this.dbPath = dbPath;
  }

  public List<Integer> getAllTreeIds() throws SQLException {
    Statement statement = connection.createStatement();
    ResultSet  resultSet = statement.executeQuery("SELECT root FROM tree");
    List<Integer> result = new ArrayList<>();
    while(resultSet.next()) {
      // read the result set
      result.add(resultSet.getInt("root"));
    }
    return result;
  }

  synchronized long saveTree(Node root) throws SQLException {
    connection.setAutoCommit(false);
    Statement statement = connection.createStatement();
    Map<Node, Long> nodeToId = new HashMap<>();
    saveNodes(statement, root, nodeToId);
    long rootId = nodeToId.get(root);
    statement.executeUpdate("UPDATE node SET treeId = " + rootId + " WHERE id IN ("
        + Joiner.on(", ").join(nodeToId.values()) + ")");
    saveEdges(statement, root, nodeToId);
    statement.executeUpdate("INSERT INTO tree VALUES (" + rootId + ")");
    connection.commit();
    connection.setAutoCommit(true);
    return rootId;
  }

  private void initialize() throws SQLException {
    Preconditions.checkState(connection == null);
    connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    connection.createStatement().executeUpdate(SCHEMA);
  }

  private void saveEdges(Statement statement, Node parent, Map<Node, Long> nodeToId) throws SQLException {
    if (parent instanceof ThreatBasedSearch.CombinationNode) {
      for (Node grandpa : ((ThreatBasedSearch.CombinationNode) parent).getParents()) {
        statement.executeUpdate("INSERT INTO combination VALUES ("
            + nodeToId.get(parent) + ", "
            + nodeToId.get(grandpa) + ")");
      }
    }
    for (Node child : parent.getChildren()) {
      statement.executeUpdate("INSERT INTO children VALUES ("
          + nodeToId.get(parent) + ", "
          + nodeToId.get(child) + ")");
      saveEdges(statement, child, nodeToId);
    }
  }

  private void saveNodes(Statement statement, Node root, Map<Node, Long> integerMap)
      throws SQLException {
    statement.executeUpdate(
        "INSERT INTO node (board, isGoal, isRefuted) VALUES ('"
            + root.getBoard().toString() + "', "
            + (root.isGoal() ? 1 : 0) + ", "
            + (root.isRefuted() ? 1 : 0) + ")");
    ResultSet rowId = statement.executeQuery("SELECT last_insert_rowid()");
    if (rowId.next()) {
      integerMap.put(root, rowId.getLong(1));
    }
    for (Node child : root.getChildren()) {
      saveNodes(statement, child, integerMap);
    }
  }

  public JSONObject loadTree(long rootId) throws SQLException {
    Statement statement = connection.createStatement();
    ResultSet nodeResult = statement.executeQuery("SELECT * FROM node WHERE treeId = " + rootId);
    Map<Long, JSONObject> idToNode = new HashMap<>();
    while (nodeResult.next()) {
      JSONObject node = new JSONObject();
      node.put("board", nodeResult.getString("board"));
      node.put("isGoal", nodeResult.getString("isGoal"));
      node.put("isRefuted", nodeResult.getString("isRefuted"));
      idToNode.put(nodeResult.getLong("id"), node);
    }
    for (Map.Entry<Long, JSONObject> e : idToNode.entrySet()) {
      long id = e.getKey();
      JSONObject node = e.getValue();
      ResultSet edgeResult = statement.executeQuery("SELECT * FROM children WHERE parentId = " + id);
      while (edgeResult.next()) {
        node.append("children", idToNode.get(edgeResult.getLong("childId")));
      }
    }
    return idToNode.get(rootId);
  }
}
