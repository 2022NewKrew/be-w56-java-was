package com.leoserver.webserver.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpParser {

  private static final Logger logger = LoggerFactory.getLogger(HttpParser.class);

  /**
   * request input stream 을 파싱하여 KakaoHttpRequest 객체를 반환한다.
   *
   * @return
   */
  public static KakaoHttpRequest toRequest(InputStream in) throws Exception {

    KakaoHttpHeader header = parseHeader(in);

    return KakaoHttpRequest.create(header);
  }

  private static KakaoHttpHeader parseHeader(InputStream in) throws Exception {

    BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

    // request line
    String requestLine = br.readLine();
    KakaoHttpHeader header = KakaoHttpHeader.create(requestLine);

    // message header
    String line = br.readLine();
    while(line != null && !line.isBlank()) {

      String[] split = line.split(": ");
      header.set(split[0], split[1]);
      line = br.readLine();

    }

    return header;
  }



}
