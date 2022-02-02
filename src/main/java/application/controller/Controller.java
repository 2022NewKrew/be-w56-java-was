package application.controller;

import http.common.HttpMethod;
import http.common.Status;
import http.request.HttpRequest;
import infrastructure.dto.AppResponse;
import application.service.UserService;
import infrastructure.dispatcher.RequestMapping;

public class Controller {

    private final String LANDING_PAGE = "/index.html";

    private final UserService userService = new UserService();

    @RequestMapping(method = HttpMethod.GET, path = "/")
    public AppResponse getHome(HttpRequest httpRequest) {
        return AppResponse.of(LANDING_PAGE, Status.OK);
    }

    @RequestMapping(method = HttpMethod.GET, path = "/user/create")
    public AppResponse getCreate(HttpRequest httpRequest) {
        return userService.getCreate(httpRequest);
    }

    @RequestMapping(method = HttpMethod.POST, path = "/user/create")
    public AppResponse postCreate(HttpRequest httpRequest) {
        return userService.postCreate(httpRequest);
    }

    @RequestMapping(method = HttpMethod.POST, path = "/user/login")
    public AppResponse postLogin(HttpRequest httpRequest) {
        return userService.postLogin(httpRequest);
    }

}
