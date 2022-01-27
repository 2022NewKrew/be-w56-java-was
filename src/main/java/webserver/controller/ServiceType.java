package webserver.controller;

import java.io.File;
import java.util.Arrays;
import java.util.function.Function;

import webserver.common.HttpMethod;
import webserver.common.Status;
import webserver.controller.request.HttpRequest;
import webserver.controller.response.HttpResponse;

public enum ServiceType {
    GET_HOME(HttpMethod.GET, "/", Service::getHome),
    GET_CREATE(HttpMethod.GET, "/user/create", Service::getCreate),
    POST_CREATE(HttpMethod.POST, "/user/create", Service::postCreate),
    GET_STATIC(HttpMethod.GET, "/", Service::getStatic),
    ERR_405(HttpMethod.UNKNOWN, "/", Service::err405);

    private final HttpMethod method;
    private final String path;
    private final Function<HttpRequest, HttpResponse> service;

    ServiceType(HttpMethod method, String path,
                Function<HttpRequest, HttpResponse> service) {
        this.method = method;
        this.path = path;
        this.service = service;
    }

    public static ServiceType findService(HttpRequest httpRequest) {
        ServiceType result;

        // 1. 메서드와 서비스 주소가 정확히 일치하면 -> 해당 서비스를 보내준다.
        result = Arrays.stream(ServiceType.values())
                       .filter(s -> (httpRequest.getPath().equals(s.path) && httpRequest.getMethod().equals(s.method)))
                       .findFirst()
                       .orElseGet(() -> null);
        if (result != null) {
            return result;
        }

        // 2. GET 메서드의 경우 : GET_STATIC 를 보내준다.(파일이 없으면 404 에러를 발생시킨다)
        if (httpRequest.getMethod() == HttpMethod.GET) {
            return ServiceType.GET_STATIC;
        }

        // 3. 1과 2에 해당하는 요청이 아닐 경우 : 403 에러를 발생시킨다.
        return ServiceType.ERR_405;
    }

    public Function<HttpRequest, HttpResponse> getService() {
        return service;
    }
}
