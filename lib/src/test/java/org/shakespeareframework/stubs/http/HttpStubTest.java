package org.shakespeareframework.stubs.http;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class HttpStubTest {

  @Test
  void get_resource() throws Exception {
    HttpResponse<String> httpResponse;
    try (var httpStub = new HttpStub().add("/things/some-key", "some thing")) {
      final var httpClient = HttpClient.newHttpClient();

      httpResponse = httpClient.send(
              HttpRequest.newBuilder(URI.create(httpStub.address() + "/things/some-key"))
                      .GET()
                      .build(),
              HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    assertThat(httpResponse.body()).isEqualTo("some thing");
    assertThat(httpResponse.statusCode()).isEqualTo(200);
  }

  @Test
  void get_unknown_resource() throws Exception {
    HttpResponse<String> httpResponse;
    try (var httpStub = new HttpStub()) {
      final var httpClient = HttpClient.newHttpClient();

      httpResponse = httpClient.send(
              HttpRequest.newBuilder(URI.create(httpStub.address() + "/things/some-key"))
                      .GET()
                      .build(),
              HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    assertThat(httpResponse.body()).isEqualTo("No resource for path /things/some-key");
    assertThat(httpResponse.statusCode()).isEqualTo(404);
  }
}
