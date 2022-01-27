package com.leoserver.webserver.http;

import com.google.gson.Gson;
import java.util.Optional;

public class KakaoHttpBody {

  Object holder;

  public KakaoHttpBody() {
    this.holder = new Object();
  }


  public KakaoHttpBody(Object object) {
    this.holder = Optional.ofNullable(object).orElseGet(Object::new);
  }


  public String toJson() {
    return new Gson().toJson(holder);
  }


  @Override
  public String toString() {
    return holder.toString();
  }

}
