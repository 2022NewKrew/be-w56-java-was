package com.leoserver.webserver.handler;

import com.leoserver.webserver.ApplicationContext;
import com.leoserver.webserver.http.HttpStatus;
import com.leoserver.webserver.http.KakaoHttpBody;
import com.leoserver.webserver.http.KakaoHttpHeader;
import com.leoserver.webserver.http.KakaoHttpResponse;
import com.leoserver.webserver.http.MIME;
import com.leoserver.webserver.http.Version;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseHandler {

  private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);
  private final ApplicationContext applicationContext;

  public ResponseHandler(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public byte[] handleResponseEntity(KakaoHttpResponse<?> response) throws IOException {

    KakaoHttpBody body = response.getBody();
    KakaoHttpHeader header = response.getHeader();
    HttpStatus status = response.getStatus();

    byte[] byteBody = body.toJson().getBytes(StandardCharsets.UTF_8);
    byte[] byteResponse = getHeader(byteBody.length, status, header);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    outputStream.write(byteResponse);
    outputStream.write(byteBody);

    return outputStream.toByteArray();
  }


  private byte[] getHeader(
      int lengthOfBodyContent,
      HttpStatus status, KakaoHttpHeader header
  ) {

    StringBuilder sb = new StringBuilder();
    String version = Version.V11.getStr();
    String code = String.valueOf(status.getCode());
    String message = status.getMessage();

    sb.append(version).append(" ").append(code).append(" ").append(message).append(" \r\n");
    header.getOptions().forEach((name, option) -> {
      sb.append(option.getKey()).append(": ").append(option.getValue()).append("\r\n");
    });
//    sb.append("Content-Type: ").append(mimeType.getContentType()).append(";charset=utf-8\r\n");
    sb.append("Content-Length: ").append(lengthOfBodyContent).append("\r\n");

    sb.append("\r\n");

    logger.debug("\n" + sb);

    return sb.toString().getBytes();
  }


}
