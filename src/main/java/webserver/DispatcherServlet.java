package webserver;

import controller.IndexController;
import controller.UserController;
import http.request.HttpRequest;
import http.request.HttpRequestMethod;
import http.response.HttpResponse;
import http.response.HttpResponse.HttpResponseBuilder;
import http.response.HttpStatusCode;
import util.UrlUtils;

/**
 * HttpRequest 의 method 와 url 을 확인해
 * <p>올바른 Controller 메서드를 실행시키고 HttpResponse 를 반환한다.
 */
public class DispatcherServlet {

    static {
        UserController.register();
        IndexController.register();
    }

    private DispatcherServlet() {}

    public static HttpResponse dispatch(HttpRequest httpRequest) {
        if (!httpRequest.validate()) {
            return new HttpResponseBuilder(HttpStatusCode.NOT_FOUND).build();
        }

        HttpRequestMethod method = httpRequest.getMethod();
        String url = UrlUtils.decode(httpRequest.getUrl());
        String trimmedUrl = UrlUtils.trimParams(url);

        return UrlMapper.get(trimmedUrl, method).apply(httpRequest);
    }
}
