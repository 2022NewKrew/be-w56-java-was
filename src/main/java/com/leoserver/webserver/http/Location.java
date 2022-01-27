package com.leoserver.webserver.http;

import static com.leoserver.webserver.http.HttpHeaderOption.HeaderOptionName.LOCATION;

import lombok.Getter;

@Getter
public class Location implements HttpHeaderOption {

  private String location;

  public Location(String location) {
    this.location = location;
  }

  @Override
  public String getKey() {
    return LOCATION.getHttpName();
  }

  @Override
  public String getValue() {
    return location;
  }

}
