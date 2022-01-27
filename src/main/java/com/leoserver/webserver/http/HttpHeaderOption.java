package com.leoserver.webserver.http;

import java.util.Arrays;
import java.util.Optional;

public interface HttpHeaderOption {

  enum HeaderOptionName {
    HOST("Host"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    LOCATION("Location");

    private String httpName;

    HeaderOptionName(String httpName) {
      this.httpName = httpName;
    }


    public static Optional<HeaderOptionName> findByHttpName(String name) {
      return Arrays.stream(values())
          .filter(option -> option.httpName.equals(name))
          .findAny();
    }


    public boolean equals(String key) {
      return httpName.equals(key);
    }


    public String getHttpName() {
      return httpName;
    }


  }

  String getKey();
  String getValue();

}
