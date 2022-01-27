package com.leoserver.webserver.http;

public class ContentLength implements HttpHeaderOption {

  private int contentLength;

  public ContentLength(int contentLength) {
    this.contentLength = contentLength;
  }


  public ContentLength(String contentLength) {
    this.contentLength = Integer.parseInt(contentLength);
  }


  public int getContentLength() {
    return contentLength;
  }

  @Override
  public String getKey() {
    return "Content-Length";
  }

  @Override
  public String getValue() {
    return String.valueOf(contentLength);
  }
}
