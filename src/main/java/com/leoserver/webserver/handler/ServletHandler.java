package com.leoserver.webserver.handler;

import com.leoserver.webserver.ApplicationContext;
import com.leoserver.webserver.annotation.RequestBody;
import com.leoserver.webserver.annotation.RequestParam;
import com.leoserver.webserver.http.KakaoHttpBody;
import com.leoserver.webserver.http.KakaoHttpHeader;
import com.leoserver.webserver.http.KakaoHttpRequest;
import com.leoserver.webserver.http.KakaoHttpResponse;
import com.leoserver.webserver.http.QueryParam;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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
      throws Exception {

    KakaoHttpHeader header = request.getHeader();
    KakaoHttpBody body = request.getBody();

    Pair<Method, Object> pairs = urlHandler.find(header.getMethod(), header.getUri().toString())
        .orElseThrow(IllegalArgumentException::new);

    Method method = pairs.getKey();
    Object bean = pairs.getValue();

    logger.debug("Servlet Mapping success : [{}] -> [{}, {}]", method, header.getMethod(),
        header.getUri());

    Parameter[] parameters = method.getParameters();
    Object[] invokeParams = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {

      handleRequestParam(invokeParams, parameters[i], i, header.getQueryParam());
      handleRequestBody(invokeParams, parameters[i], i, body);

    }

    KakaoHttpResponse<?> response = (KakaoHttpResponse<?>) method.invoke(bean, invokeParams);
    return responseHandler.handleResponseEntity(response);
  }


  private void handleRequestParam(
      Object[] invokeParams, Parameter parameter,
      int index, QueryParam queryParam
  ) {

    RequestParam requestParam = parameter.getAnnotation(RequestParam.class);

    if (requestParam == null) {
      return;
    }

    String paramKey = requestParam.value();

    invokeParams[index] = queryParam.get(paramKey)
        .orElseThrow(IllegalArgumentException::new);    // queryParam 의 요청값이 없음.
  }


  private void handleRequestBody(
      Object[] invokeParams, Parameter parameter,
      int index, KakaoHttpBody body
  ) throws Exception {

    RequestBody requestBody = parameter.getAnnotation(RequestBody.class);

    if (requestBody == null) {
      return;
    }


    logger.debug("Parameter Type : {}", parameter.getClass());
    Class<?> cls = parameter.getType();
    Constructor<?> constructor = cls.getConstructor();
    Object object = constructor.newInstance();
    Field[] fields = object.getClass().getDeclaredFields();
    for (Field field : fields) {

      String injectParam = body.getFieldAsString(field.getName());
      field.setAccessible(true);
      field.set(object, injectParam);

      logger.debug("parameter body objects : {}", injectParam);
    }

    invokeParams[index] = object;
  }


}
