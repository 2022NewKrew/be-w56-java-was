package com.leoserver.webserver.http;

import lombok.Getter;

@Getter
public class KakaoHttpResponse<T> {

  private KakaoHttpHeader header;
  private KakaoHttpBody body;
  private HttpStatus status;

  public KakaoHttpResponse(HttpStatus status, T body) {
    this(status, body, MIME.APPLICATION_JSON);
  }


  public KakaoHttpResponse(HttpStatus status, T body, MIME mimeType) {
    this.body = KakaoHttpBody.create(body);
    this.status = status;
    this.header = KakaoHttpHeader.createResponse(mimeType);
  }


  public KakaoHttpResponse(HttpStatus status, T body, KakaoHttpHeader header) {
    this.body = KakaoHttpBody.create(body);
    this.status = status;
    this.header = header;
  }


}
