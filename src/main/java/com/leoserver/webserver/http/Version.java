package com.leoserver.webserver.http;

import java.util.Arrays;
import java.util.Optional;
import lombok.ToString;

@ToString
public enum Version {

  V10("HTTP/1.0"),
  V11("HTTP/1.1"),
  V20("HTTP/2.0");

  private String str;

  Version(String str) {
    this.str = str;
  }


  public static Version of(String requestLineVersion) {

    Optional.ofNullable(requestLineVersion).orElseThrow(IllegalArgumentException::new);

    return Arrays.stream(values())
        .filter(version -> version.str.equals(requestLineVersion))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }


  public String getStr() {
    return str;
  }

}
