package com.leoserver.webserver.http;

import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class KakaoHttpRequest {

  private UUID id;
  private KakaoHttpHeader header;
  private KakaoHttpBody<?> body;

  private KakaoHttpRequest() {
    this.id = UUID.randomUUID();
  }


  public static KakaoHttpRequest create(KakaoHttpHeader header, KakaoHttpBody body) {
    KakaoHttpRequest request = new KakaoHttpRequest();
    request.header = Optional.ofNullable(header).orElseThrow(IllegalArgumentException::new);
    request.body = Optional.ofNullable(body).orElseThrow(IllegalArgumentException::new);
    return request;
  }


  public static KakaoHttpRequest create(KakaoHttpHeader header) {
    return create(header, new KakaoHttpBody<>());
  }


  public Uri getUri() {
    return header.getUri();
  }

}
