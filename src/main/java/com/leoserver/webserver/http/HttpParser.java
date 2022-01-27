package com.leoserver.webserver.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class HttpParser {

  private static final Logger logger = LoggerFactory.getLogger(HttpParser.class);
  private final BufferedReader reader;


  public HttpParser(InputStream in) {
    this.reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
  }


  /**
   * request input stream 을 파싱하여 KakaoHttpRequest 객체를 반환한다.
   *
   * @return
   */
  public KakaoHttpRequest toRequest() throws Exception {

    KakaoHttpHeader header = parseHeader();
    KakaoHttpBody body = parseBody(header);

    return KakaoHttpRequest.create(header, body);
  }

  private KakaoHttpHeader parseHeader() throws Exception {

    // request line
    String requestLine = reader.readLine();
    KakaoHttpHeader header = KakaoHttpHeader.createRequest(requestLine);

    logger.debug("requestLine : {}", requestLine);

    // message header
    String line = reader.readLine();
    while (line.length() > 0) {

      logger.debug("header : {}", line);

      String[] split = line.split(": ");
      header.set(split[0], split[1]);
      line = reader.readLine();

    }

    return header;
  }


  private KakaoHttpBody parseBody(KakaoHttpHeader header) throws Exception {

    int length = header.getContentLength();
    char[] buffer = new char[length];
    int eof = reader.read(buffer, 0, length);
    String body = String.valueOf(buffer);

    logger.debug("body : {}", body);

    return KakaoHttpBody.create(toObjectByMIME(body, header.getContentType()));
  }


  private JsonObject toObjectByMIME(String body, MIME contentType) {

    if (MIME.APPLICATION_X_WWW_FORM_URLENCODED == contentType) {
      String decoded = URLDecoder.decode(body, StandardCharsets.UTF_8);
      Map<String, String> map = HttpRequestUtils.parseQueryString(decoded);
      return new Gson().toJsonTree(map).getAsJsonObject();
    }

    return null;
  }


}
