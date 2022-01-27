package com.leoserver.webserver.handler;

import com.leoserver.webserver.http.HttpStatus;
import com.leoserver.webserver.http.KakaoHttpHeader;
import com.leoserver.webserver.http.KakaoHttpRequest;
import com.leoserver.webserver.http.MIME;
import com.leoserver.webserver.http.Uri;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

public class StaticResourceHandler {

  private static final Logger logger = LoggerFactory.getLogger(StaticResourceHandler.class);

  public byte[] handle(KakaoHttpRequest request) throws IOException {

    Uri uri = request.getUri();
    KakaoHttpHeader header = request.getHeader();

    File file = new File("./webapp" + uri.toString());
    String extension = IOUtils.getExtension(uri.toString())
        .orElseThrow(IllegalArgumentException::new);
    MIME mimeType = MIME.getNameByExtension(extension);

    if (!file.exists()) {
      return getHeader(0, header, HttpStatus.NOT_FOUND, mimeType);
    }

    byte[] body = Files.readAllBytes(file.toPath());
    byte[] response = getHeader(body.length, header, HttpStatus.OK, mimeType);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    outputStream.write(response);
    outputStream.write(body);

    return outputStream.toByteArray();
  }


  private byte[] getHeader(
      int lengthOfBodyContent, KakaoHttpHeader header,
      HttpStatus status, MIME mimeType
  ) {

    StringBuilder sb = new StringBuilder();
    String version = header.getVersion().getStr();
    String code = String.valueOf(status.getCode());
    String message = status.getMessage();

    sb.append(version).append(" ").append(code).append(" ").append(message).append(" \r\n");
    sb.append("Content-Type: ").append(mimeType.toHttp()).append(";charset=utf-8\r\n");
    sb.append("Content-Length: ").append(lengthOfBodyContent).append("\r\n");
    sb.append("\r\n");

    logger.debug("\n ResponseHeader : {}", sb);

    return sb.toString().getBytes();
  }

}
