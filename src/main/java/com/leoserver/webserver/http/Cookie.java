package com.leoserver.webserver.http;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cookie {

  String key;
  Object value;
  String path;

  public Cookie(String key, Object value, String path) {
    this.key = key;
    this.value = value;
    this.path = path;
  }

}
