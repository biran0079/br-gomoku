package server;

import ai.threatbasedsearch.DatabaseManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.sql.SQLException;

/**
 * Request handler to render trees.
 */
public class TreesRequestHandler implements HttpHandler {
  private final DatabaseManager databaseManager;

  TreesRequestHandler(DatabaseManager databaseManager) {
    this.databaseManager = databaseManager;
  }

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    URI uri = httpExchange.getRequestURI();
    String path = uri.getPath();
    if (path.equals("/trees")) {
      JSONArray jsonArray;
      try {
        jsonArray = new JSONArray(databaseManager.getAllTreeIds().toArray());
      } catch (SQLException e) {
        throw new IOException(e);
      }
      httpExchange.sendResponseHeaders(200, 0);
      try (OutputStream os = httpExchange.getResponseBody()) {
        os.write(jsonArray.toString().getBytes());
      }
    } else if (path.startsWith("/trees/")) {
      long treeId = Long.valueOf(path.substring("/trees/".length()));
      httpExchange.sendResponseHeaders(200, 0);
      try (OutputStream os = httpExchange.getResponseBody()) {
        os.write(databaseManager.loadTree(treeId).toString().getBytes());
      } catch (SQLException e) {
        throw new IOException(e);
      }
    } else {
      String response = "404 (Not Found)\n";
      httpExchange.sendResponseHeaders(404, response.length());
      try (OutputStream os = httpExchange.getResponseBody()) {
        os.write(response.getBytes());
      }
    }
  }
}
