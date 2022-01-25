package controller;

import util.ModelAndView;
import webserver.HttpRequest;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class Controller {
    protected Map<String, ControllerMethod> runner = new HashMap<>();
    protected String baseUrl;


    public ModelAndView execute(HttpRequest httpRequest, DataOutputStream dos) {
        return runner.get(httpRequest.getMethod() + httpRequest.getRequestUri())
                .run(httpRequest, dos);
    }

    public boolean isSupport(String url) {
        return url.startsWith(baseUrl);
    }

}
