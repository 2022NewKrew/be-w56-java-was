package com.leoserver.webserver.http;

public class Uri {

  String uri;

  public Uri(String uri) {
    isValid(uri);
    this.uri = trim(uri);
  }


  private void isValid(String uri) {
    //TODO need to valid check
  }


  private String trim(String uri) {
    //TODO need to do more
    uri = uri.strip();
    uri = separateQueryString(uri);
    return uri;
  }


  private String separateQueryString(String uri) {

    if(uri.contains("?")) {
      return uri.substring(0, uri.indexOf("?"));
    }

    return uri;
  }


  @Override
  public String toString() {
    return uri;
  }


  public boolean hasExtension() {
    return uri.contains(".");
  }

}
