package server;

import ai.AI;
import ai.minmax.MinMaxSearch;
import ai.minmax.transitiontable.SmartTransitionTable;
import ai.threatbasedsearch.ThreatBasedAI;
import com.google.common.collect.ImmutableMap;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import common.StoneType;
import common.boardclass.testing.BoardClassUtil;
import model.GameBoard;
import model.Position;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

/**
 * HTTP request handler.
 */
class RequestHandler implements HttpHandler {

  private static JSONObject parseRequest(HttpExchange httpExchange)
      throws IOException {
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(httpExchange.getRequestBody()))) {
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }
      return new JSONObject(sb.toString());
    }
  }

  private boolean eq(String s1, String s2) {
    return s1.toLowerCase().equals(s2.toLowerCase());
  }

  private final AI novice = new ThreatBasedAI(
      MinMaxSearch.defaultBuilderForThreat()
          .withTransitionTableFactory(SmartTransitionTable::new)
          .withAlgorithm(MinMaxSearch.Algorithm.MINMAX)
          .withMaxDepth(4)
          .useKillerHeuristic()
          .build(),
      3);

  private final AI medium = new ThreatBasedAI(
      MinMaxSearch.defaultBuilderForThreat()
          .withTransitionTableFactory(SmartTransitionTable::new)
          .withAlgorithm(MinMaxSearch.Algorithm.MINMAX)
          .withMaxDepth(6)
          .useKillerHeuristic()
          .build(),
      4);

  private final AI expert = new ThreatBasedAI(
      MinMaxSearch.defaultBuilderForThreat()
          .withTransitionTableFactory(SmartTransitionTable::new)
          .withAlgorithm(MinMaxSearch.Algorithm.MINMAX)
          .withMaxDepth(7)
          .useKillerHeuristic()
          .build(),
      5);

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    System.err.println(httpExchange.getRequestMethod());
    try {
      if (eq(httpExchange.getRequestMethod(), "post")) {
        JSONObject request = parseRequest(httpExchange);
        GameBoard board = BoardClassUtil.fromString(request.getString("board"));
        StoneType nextToMove = eq(request.getString("next"), "black")
            ? StoneType.BLACK : StoneType.WHITE;
        AI ai;
        String level = request.getString("level").toLowerCase();
        switch (level) {
          case "novice":
            ai = novice;
            break;
          case "expert":
            ai = expert;
            break;
          case "medium":
            ai = medium;
            break;
          default:
            ai = novice;
            System.err.println("unkonwn level: " + level);
            break;
        }
        Position position = ai.nextMove(board, nextToMove);
        Map<String, Object> param = ImmutableMap.<String, Object>builder()
            .put("row", position.getRowIndex())
            .put("column", position.getColumnIndex())
            .build();
        String response  = new JSONObject(param).toString();
        System.err.println(response);
        httpExchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = httpExchange.getResponseBody()) {
          os.write(response.getBytes());
        }
      } else {
        String response = "Please POST me!";
        httpExchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = httpExchange.getResponseBody()) {
          os.write(response.getBytes());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
