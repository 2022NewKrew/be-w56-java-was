package com.leoserver.webserver.http;

import lombok.ToString;

@ToString
public enum HttpStatus {

  // TODO
  // 모두 구현은 시간날 때..
  // 일단은 필요할 때마다 추가

  OK(200, "OK"),
  NOT_FOUND(404, "Not Found");

  private int code;
  private String message;
  HttpStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
