package com.leoserver.webserver.http;

import java.util.Arrays;

public enum MIME implements HttpHeaderOption {

  // TODO
  // 모두 구현은 시간날 때..
  // 일단은 필요할 때마다 추가

  TEXT_PLAIN("text/plain"),
  TEXT_HTML("text/html", "htm", "html"),
  TEXT_JAVASCRIPT("text/javascript", "js"),
  TEXT_CSS("text/css", "css"),
  FONT_WOFF("font/woff", "woff"),
  FONT_WOFF2("font/woff2", "woff2"),
  FONT_TTF("font/ttf", "ttf"),
  IMAGE_X_ICON("image/x-icon", "ico"),
  IMAGE_JPEG("image/jpeg", "jpeg", "jpg"),
  APPLICATION_OCTET_STREAM("application/octet-stream"),
  APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
  APPLICATION_JSON("application/json", "json");


  private String value;
  private String[] extensions;

  MIME(String value, String... extensions) {
    this.value = value;
    this.extensions = extensions;
  }


  public static MIME from(String contentType) {
    return Arrays.stream(values())
        .filter(type -> type.value.equals(contentType))
        .findAny()
        .orElseThrow(IllegalArgumentException::new);    // 지원 타입 아님
  }


  public static MIME getNameByExtension(String extension) {
    return Arrays.stream(values())
        .filter(mime -> {
          for (String ext : mime.extensions) {
            if (ext.equals(extension)) {
              return true;
            }
          }
          return false;
        }).findAny()
        .orElse(APPLICATION_OCTET_STREAM);
  }


  public String toHttp() {
    return value;
  }


  @Override
  public String getKey() {
    return "Content-Type";
  }

  @Override
  public String getValue() {
    return value;
  }

}
