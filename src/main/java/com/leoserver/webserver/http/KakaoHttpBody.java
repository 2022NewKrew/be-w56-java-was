package com.leoserver.webserver.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class KakaoHttpBody {

  JsonObject holder;


  private KakaoHttpBody(Object object) {

    if(object == null) {
      object = new Object();
    }

    this.holder = (JsonObject) new Gson().toJsonTree(object);
  }


  public static KakaoHttpBody createEmpty() {
    return new KakaoHttpBody(null);
  }


  public static KakaoHttpBody create(Object body) {
    return new KakaoHttpBody(body);
  }


  public String toJson() {
    return new Gson().toJson(holder);
  }


  public String getFieldAsString(String key) {
    return holder.get(key).getAsString();
  }


  @Override
  public String toString() {
    return holder.toString();
  }

}
