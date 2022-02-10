package webserver.controller;

import webserver.http.response.HttpResponse;
import webserver.http.domain.HttpMethod;
import webserver.http.domain.MethodAndUrl;
import webserver.http.request.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller {
    protected Map<MethodAndUrl, ControllerMethod> runner = new HashMap<>();


    public void execute(HttpRequest httpRequest, HttpResponse res) {
        runner.get(new MethodAndUrl(HttpMethod.valueOf(httpRequest.getMethod()) , httpRequest.getRequestUri()))
                .run(httpRequest, res);
    }

    public boolean isSupport(HttpRequest req) {
        return runner.keySet().stream().filter(k-> k.checkRequest(req)).count() >= 1;
    }

}
