package com.leoserver.webserver.handler;

import com.leoserver.webserver.ApplicationContext;
import com.leoserver.webserver.annotation.RequestParam;
import com.leoserver.webserver.http.KakaoHttpHeader;
import com.leoserver.webserver.http.KakaoHttpRequest;
import com.leoserver.webserver.http.KakaoHttpResponse;
import com.leoserver.webserver.http.QueryParam;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils.Pair;

public class ServletHandler {

  private static final Logger logger = LoggerFactory.getLogger(ServletHandler.class);
  private final ApplicationContext applicationContext;
  private final UrlHandler urlHandler;
  private final ResponseHandler responseHandler;

  public ServletHandler(ApplicationContext applicationContext) throws Exception {
    this.applicationContext = applicationContext;
    this.urlHandler = new UrlHandler(applicationContext);
    this.responseHandler = new ResponseHandler(applicationContext);
  }


  public boolean hasMappedMethod(KakaoHttpRequest request) {
    KakaoHttpHeader header = request.getHeader();
    return urlHandler.find(header.getMethod(), header.getUri().toString())
        .isPresent();
  }


  public byte[] handle(KakaoHttpRequest request)
      throws InvocationTargetException, IllegalAccessException, IOException {

    KakaoHttpHeader header = request.getHeader();

    Pair<Method, Object> pairs = urlHandler.find(header.getMethod(), header.getUri().toString())
        .orElseThrow(IllegalArgumentException::new);

    Method method = pairs.getKey();
    Object bean = pairs.getValue();

    logger.debug("Servlet Mapping success : [{}] -> [{}, {}]", method, header.getMethod(),
        header.getUri());

    Parameter[] parameters = method.getParameters();
    Object[] invokeParams = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {

      Parameter parameter = parameters[i];
      RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
      String paramKey = requestParam.value();

      QueryParam queryParam = header.getQueryParam();
      invokeParams[i] = queryParam.get(paramKey)
          .orElseThrow(IllegalArgumentException::new);    // queryParam 의 요청값이 없음.

    }

    KakaoHttpResponse<?> response = (KakaoHttpResponse<?>) method.invoke(bean, invokeParams);
    return responseHandler.handleResponseEntity(response);
  }


}
