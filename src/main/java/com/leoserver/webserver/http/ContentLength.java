package com.leoserver.webserver.http;

import static com.leoserver.webserver.http.HttpHeaderOption.HeaderOptionName.CONTENT_LENGTH;

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
    return CONTENT_LENGTH.getHttpName();
  }

  @Override
  public String getValue() {
    return String.valueOf(contentLength);
  }
}
