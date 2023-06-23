package org.shakespeareframework.stubs.http;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class HttpStub implements AutoCloseable {

  private final HttpServer httpServer;
  private final Map<String, String> resourceMap = new HashMap<>();

  public HttpStub() {
    try {
      httpServer = HttpServer.create(new InetSocketAddress(0), 0);
      httpServer.start();
      httpServer.createContext("/", httpExchange -> {
            final var path = httpExchange.getRequestURI().getPath();
            String responseBody;
            if (!resourceMap.containsKey(path)) {
                responseBody = "No resource for path %s".formatted(path);
                httpExchange.sendResponseHeaders(404, 0);
            } else {
                responseBody = resourceMap.get(path);
                httpExchange.sendResponseHeaders(200, responseBody.length());
            }
            try (var responseBodyOutputStream = httpExchange.getResponseBody()) {
                responseBodyOutputStream.write(responseBody.getBytes());
            }
            httpExchange.close();
        });
    } catch (IOException e) {
      throw new HttpStubCreationException(e);
    }
  }

  public String address() {
    return "http:/%s".formatted(httpServer.getAddress());
  }
  
  public HttpStub add(String path, String body) {
      resourceMap.put(path, body);
      return this;
  }

  public HttpStub add(Map<String, String> add) {
    resourceMap.putAll(add);
    return this;
  }

  @Override
  public void close() {
    httpServer.stop(0);
  }
}
