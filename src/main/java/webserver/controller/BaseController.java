package webserver.controller;

import http.exception.UnsupportedHttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;

public class BaseController implements Controller {

    @Override
    public HttpResponse service(HttpRequest request) {
        switch (request.getMethod()) {
            case GET:
                return get(request);

            case POST:
                return post(request);

            case PUT:
            case DELETE:
            case CONNECT:
            case OPTIONS:
            case TRACE:
            case PATCH:
            default:
                throw new UnsupportedHttpMethod();
        }
    }

    public HttpResponse get(HttpRequest request) {
        return HttpResponse.ok(request.getUri());
    }

    public HttpResponse post(HttpRequest request) {
        return HttpResponse.ok(request.getUri());
    }
}
