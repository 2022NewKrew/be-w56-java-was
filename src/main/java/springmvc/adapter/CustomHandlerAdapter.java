package springmvc.adapter;

import springmvc.frontcontroller.CustomModelView;
import webserver.http.request.CustomHttpRequest;
import webserver.http.response.CustomHttpResponse;

public interface CustomHandlerAdapter {
    boolean supports(Object handler);

    CustomModelView handle(CustomHttpRequest request, CustomHttpResponse response, Object handler);
}
