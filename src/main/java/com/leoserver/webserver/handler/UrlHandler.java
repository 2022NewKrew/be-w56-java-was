package com.leoserver.webserver.handler;

import com.leoserver.webserver.ApplicationContext;
import com.leoserver.webserver.annotation.Controller;
import com.leoserver.webserver.annotation.RequestMapping;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils.Pair;

public class UrlHandler {

  private static final Logger logger = LoggerFactory.getLogger(UrlHandler.class);
  private final ApplicationContext applicationContext;
  private final Map<String, Pair<Method, Object>> urlMapper;

  public UrlHandler(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.urlMapper = new HashMap<>();
    setMapper();
    logger.debug("successfully url mapped : {}", urlMapper);
  }


  public Optional<Pair<Method, Object>> find(com.leoserver.webserver.http.Method method, String uri) {
    String key = getUrlKey(method, uri);
    return Optional.ofNullable(urlMapper.get(key));
  }


  private void setMapper() {

    Set<Object> beans = applicationContext.findByAnnotation(Controller.class);

    // Controller bean 들의 urlMethod 를 찾아 매핑
    for (Object bean : beans) {
      mapUrlByMethods(bean);
    }

  }


  private void mapUrlByMethods(Object bean) {

    Set<Method> urlMethods = findRequestMappedMethods(bean);

    for (Method method : urlMethods) {
      RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
      String key = getUrlKey(requestMapping);
      put(key, method, bean);
    }

  }


  private void put(String key, Method method, Object bean) {

    if(urlMapper.containsKey(key)) {
      // method, path 중복
      throw new IllegalArgumentException();
    }

    urlMapper.put(key, new Pair<>(method, bean));
  }


  private Set<Method> findRequestMappedMethods(Object bean) {
    return Arrays.stream(bean.getClass().getMethods())
        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
        .collect(Collectors.toUnmodifiableSet());
  }


  private String getUrlKey(RequestMapping requestMapping) {
    return getUrlKey(requestMapping.method(), requestMapping.path());
  }


  private String getUrlKey(com.leoserver.webserver.http.Method method, String uri) {
    return method + uri;
  }

}
