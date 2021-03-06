package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * Serves static files.
 */
public class StaticFileHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    String root = "./resources";
    URI uri = httpExchange.getRequestURI();
    System.out.println("looking for: "+ root + uri.getPath());
    String path = uri.getPath();
    File file = new File(root + path).getCanonicalFile();

    if (!file.isFile()) {
      // Object does not exist or is not a file: reject with 404 error.
      String response = "404 (Not Found)\n";
      httpExchange.sendResponseHeaders(404, response.length());
      try (OutputStream os = httpExchange.getResponseBody()) {
        os.write(response.getBytes());
      }
    } else {
      // Object exists and is a file: accept with response code 200.
      String mime = "text/html";
      if (path.endsWith(".js")) {
        mime = "application/javascript";
      }
      if (path.endsWith(".css")) {
        mime = "text/css";
      }

      Headers h = httpExchange.getResponseHeaders();
      h.set("Content-Type", mime);
      httpExchange.sendResponseHeaders(200, 0);

      try (OutputStream os = httpExchange.getResponseBody();
          FileInputStream fs = new FileInputStream(file)) {
        final byte[] buffer = new byte[0x10000];
        int count;
        while ((count = fs.read(buffer)) >= 0) {
          os.write(buffer, 0, count);
        }
      }
    }
  }
}
