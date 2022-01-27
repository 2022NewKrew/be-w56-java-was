package webserver.controller;


import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.common.FileLocation;
import webserver.common.HttpMethod;
import webserver.common.Status;
import webserver.controller.request.HttpRequest;
import webserver.controller.response.HttpResponse;
import webserver.service.StaticService;
import webserver.service.UserService;

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private final StaticService staticService = new StaticService();
    private final UserService userService = new UserService();

    public HttpResponse getStatic(HttpRequest httpRequest) {
        return staticService.getStatic(httpRequest);
    }

    @RequestMapping(method = HttpMethod.GET, path = "/")
    public HttpResponse getHome(HttpRequest httpRequest) {
        return HttpResponse.of(FileLocation.INDEX.path, Status.OK);
    }

    @RequestMapping(method = HttpMethod.GET, path = "/user/create")
    public HttpResponse getCreate(HttpRequest httpRequest) {
        return userService.getCreate(httpRequest);
    }

    @RequestMapping(method = HttpMethod.POST, path = "/user/create")
    public HttpResponse postCreate(HttpRequest httpRequest) {
        return userService.postCreate(httpRequest);
    }

    @RequestMapping(method = HttpMethod.POST, path = "/user/login")
    public HttpResponse postLogin(HttpRequest httpRequest) {
        return userService.postLogin(httpRequest);
    }

    public HttpResponse err405(HttpRequest httpRequest) {
        return HttpResponse.of(FileLocation.NONE.path, Status.NOT_ALLOWED);
    }
}
