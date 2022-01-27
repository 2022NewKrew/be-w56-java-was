package com.leoserver.webserver.http;

import static com.leoserver.webserver.http.HttpHeaderOption.HeaderOptionName.CONTENT_LENGTH;
import static com.leoserver.webserver.http.HttpHeaderOption.HeaderOptionName.CONTENT_TYPE;
import static com.leoserver.webserver.http.HttpHeaderOption.HeaderOptionName.HOST;
import static com.leoserver.webserver.http.HttpHeaderOption.HeaderOptionName.findByHttpName;

import com.leoserver.webserver.http.HttpHeaderOption.HeaderOptionName;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoHttpHeader {

  private Method method;
  private Uri uri;
  private Version version;
  private QueryParam queryParam;
  private LocalDateTime date;

  private Map<HeaderOptionName, HttpHeaderOption> options;


  private KakaoHttpHeader() {
    this.date = LocalDateTime.now();
    this.options = new HashMap<>();
  }


  public static KakaoHttpHeader createRequest(
      Method method, Uri uri, QueryParam queryParam, Version version
  ) {
    KakaoHttpHeader header = new KakaoHttpHeader();
    header.method = method;
    header.uri = uri;
    header.queryParam = queryParam;
    header.version = version;
    return header;
  }


  public static KakaoHttpHeader createRequest(String requestLine) throws Exception {

    String[] split = requestLine.split(" ");

    try{

      Method method = Method.valueOf(split[0]);
      Uri uri = new Uri(split[1]);
      QueryParam queryParam = QueryParam.create(split[1]);
      Version version = Version.of(split[2]);

      return createRequest(method, uri, queryParam, version);

    }catch(Exception e) {
      //FIXME exception 구체화
      throw new Exception();
    }

  }


  public static KakaoHttpHeader createResponse(MIME mimeType) {
    KakaoHttpHeader header = new KakaoHttpHeader();
    header.set(mimeType.getKey(), mimeType.getValue());
    return header;
  }


  public static KakaoHttpHeader createResponse() {
    return createResponse(MIME.APPLICATION_OCTET_STREAM);
  }


  public void set(HeaderOptionName name, HttpHeaderOption option) {
    options.put(name, option);
  }


  public void set(String key, String value) {

    if(HOST.equals(key)) {
      options.put(findByHttpName(key).orElseThrow(), new Host(value));
      return;
    }

    if(CONTENT_LENGTH.equals(key)) {
      options.put(findByHttpName(key).orElseThrow(), new ContentLength(value));
      return;
    }

    if(CONTENT_TYPE.equals(key)) {
      options.put(findByHttpName(key).orElseThrow(), MIME.from(value));
      return;
    }

  }


  public int getContentLength() {
    ContentLength contentLength = (ContentLength) options.get(CONTENT_LENGTH);
    return Optional.ofNullable(contentLength)
        .map(ContentLength::getContentLength)
        .orElse(0);
  }


  public MIME getContentType() {
    MIME contentType = (MIME) options.get(CONTENT_TYPE);
    return Optional.ofNullable(contentType)
        .orElse(MIME.APPLICATION_OCTET_STREAM);
  }


}
