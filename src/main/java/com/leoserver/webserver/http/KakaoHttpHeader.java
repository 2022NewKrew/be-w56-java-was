package com.leoserver.webserver.http;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KakaoHttpHeader {

  private Method method;
  private Uri uri;
  private Host host;
  private int contentLength;
  private MIME contentType;
  private Version version;
  private QueryParam queryParam;
  private LocalDateTime date;

  private KakaoHttpHeader() {
    this.date = LocalDateTime.now();
  }


  public static KakaoHttpHeader create(
      Method method, Uri uri, QueryParam queryParam, Version version
  ) {
    KakaoHttpHeader header = new KakaoHttpHeader();
    header.method = method;
    header.uri = uri;
    header.queryParam = queryParam;
    header.version = version;
    return header;
  }


  public static KakaoHttpHeader create(String requestLine) throws Exception {

    String[] split = requestLine.split(" ");

    try{

      Method method = Method.valueOf(split[0]);
      Uri uri = new Uri(split[1]);
      QueryParam queryParam = QueryParam.create(split[1]);
      Version version = Version.of(split[2]);

      return create(method, uri, queryParam, version);

    }catch(Exception e) {
      //FIXME exception 구체화
      throw new Exception();
    }

  }


  public void set(String key, String value) {

    if("Host".equals(key)) {
      this.host = new Host(value);
      return;
    }

    if("Content-Length".equals(key)) {
      this.contentLength = Integer.parseInt(value);
      return;
    }

    if("Content-Type".equals(key)) {
      this.contentType = MIME.from(value);
      return;
    }

  }

}
