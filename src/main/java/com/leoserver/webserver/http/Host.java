package com.leoserver.webserver.http;

public class Host {

  String url;

  public Host(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return url;
  }
}
