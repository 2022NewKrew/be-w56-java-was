package com.leoserver.webserver.http;

import lombok.Getter;

@Getter
public class KakaoHttpResponse<T> {

  private KakaoHttpBody<T> body;
  private HttpStatus status;
  private MIME mimeType;

  public KakaoHttpResponse(HttpStatus status, T body) {
    this(status, body, MIME.APPLICATION_JSON);
  }


  public KakaoHttpResponse(HttpStatus status, T body, MIME mimeType) {
    this.body = new KakaoHttpBody<>(body);
    this.status = status;
    this.mimeType = mimeType;
  }

}
