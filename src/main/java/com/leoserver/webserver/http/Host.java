package com.leoserver.webserver.http;

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
    return "Host";
  }

  @Override
  public String getValue() {
    return url;
  }

}
