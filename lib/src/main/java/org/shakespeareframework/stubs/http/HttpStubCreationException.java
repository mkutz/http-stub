package org.shakespeareframework.stubs.http;

import java.io.IOException;

public class HttpStubCreationException extends RuntimeException {

  public HttpStubCreationException(IOException e) {
    super(e);
  }
}
