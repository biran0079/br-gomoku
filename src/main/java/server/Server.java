package server;

import ai.threatbasedsearch.DatabaseManager;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Http server for gomoku AI.
 */
public class Server {

  public static void main(String[] args) throws Exception {
    HttpServer server = HttpServer.create(new InetSocketAddress(9528), 0);
    server.createContext("/static/", new StaticFileHandler());
    server.createContext("/ai", new AIRequestHandler());
    server.createContext("/trees", new TreesRequestHandler(DatabaseManager.fileDb()));
    server.setExecutor(
        Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()));
    server.start();
  }
}
