package com.leoserver.webserver.http;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import util.HttpRequestUtils;

public class QueryParam {

  private final Map<String, String> params;

  private QueryParam() {
    this.params = new HashMap<>();
  }


  public static QueryParam createEmpty() {
    return new QueryParam();
  }


  public static QueryParam create(String uri) {
    QueryParam queryParam = new QueryParam();
    queryParam.setParams(uri);
    return queryParam;
  }


  public void putAll(Map<String, String> params) {
    this.params.putAll(params);
  }


  public Optional<String> get(String key) {
    return Optional.ofNullable(params.get(key));
  }


  private void setParams(String uri) {
    if (!uri.contains("?")) {
      return;
    }

    String queryString = uri.substring(uri.indexOf("?") + 1);
    String decoded = URLDecoder.decode(queryString, StandardCharsets.UTF_8);
    putAll(HttpRequestUtils.parseQueryString(decoded));
  }


  @Override
  public String toString() {
    return params.toString();
  }

}
