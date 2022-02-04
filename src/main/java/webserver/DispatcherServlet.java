package webserver;

import controller.IndexController;
import controller.UserController;
import request.HttpRequest;
import response.HttpResponse;
import util.UrlUtils;

/**
 * HttpRequest 의 method 와 url 을 확인해 올바른 Controller 메서드를 찾아 실행시키고 HttpResponse 를 반환한다.
 */
public class DispatcherServlet {

    private static final UrlMapper URL_MAPPER = UrlMapper.getInstance();

    static {
        UserController.register();
        IndexController.register();
    }

    private DispatcherServlet() {}

    public static HttpResponse dispatch(HttpRequest httpRequest) {
        if (!httpRequest.validate()) {
            return new HttpResponse();
        }

        String method = httpRequest.getMethod();
        String url = UrlUtils.decode(httpRequest.getUrl());
        String trimmedUrl = UrlUtils.trimParams(url);

        return URL_MAPPER.get(trimmedUrl, method).apply(httpRequest);
    }
}
