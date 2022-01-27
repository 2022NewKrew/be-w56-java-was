package com.leoserver.webserver.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Cookies implements Iterable<Map.Entry<String, Cookie>> {

  private final Map<String, Cookie> holder;

  public Cookies() {
    this.holder = new HashMap<>();
  }


  public void put(String key, Cookie value) {
    holder.put(key, value);
  }


  public Cookie get(String key) {
    return holder.get(key);
  }


  @Override
  public Iterator<Entry<String, Cookie>> iterator() {
    return holder.entrySet().iterator();
  }

}
