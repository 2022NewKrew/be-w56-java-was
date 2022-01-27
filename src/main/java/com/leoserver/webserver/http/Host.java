package com.leoserver.webserver.http;

import static com.leoserver.webserver.http.HttpHeaderOption.HeaderOptionName.HOST;

public class Host implements HttpHeaderOption {

  String url;

  public Host(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return url;
  }

  @Override
  public String getKey() {
    return HOST.getHttpName();
  }

  @Override
  public String getValue() {
    return url;
  }

}
