package com.leoserver.webserver.http;

import com.google.gson.Gson;

public class KakaoHttpBody<T> {

  T holder;

  public KakaoHttpBody() {
    this.holder = null;
  }


  public KakaoHttpBody(T object) {
    this.holder = object;
  }


  public String toJson() {
    return new Gson().toJson(holder);
  }


}
