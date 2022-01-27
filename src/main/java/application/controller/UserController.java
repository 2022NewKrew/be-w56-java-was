package application.controller;

import application.dto.UserCreateRequest;
import application.dto.UserLoginRequest;
import application.service.UserService;
import common.controller.AbstractController;
import common.controller.ControllerType;
import common.dto.ControllerRequest;
import common.dto.ControllerResponse;
import lombok.extern.slf4j.Slf4j;
import webserver.dto.response.HttpStatus;

import java.util.Map;

@Slf4j
public class UserController extends AbstractController {

    @Override
    public ControllerResponse doService(ControllerRequest request) {
        log.debug("[" + UserController.log.getName() + "] doService()");

        return switch (request.getHttpMethod()) {
            case GET -> doGet(request);
            case POST -> doPost(request);
        };
    }

    private ControllerResponse doGet(ControllerRequest request) {
        // TODO
        return null;
    }

    private ControllerResponse doPost(ControllerRequest request) {
        return switch (request.getUrl()) {
            case "/users" -> createUser(request);
            case "/users/login" -> loginUser(request);
            default -> throw new IllegalArgumentException();
        };
    }

    public ControllerResponse createUser(ControllerRequest request) {
        HttpStatus httpStatus;
        String redirectTo;

        try {
            UserService.create(UserCreateRequest.mapToUserCreateRequest(request.getBody()));
            request.getHeader().put("Location", "http://localhost:8080/index.html");
            redirectTo = ControllerType.STATIC.getUrl() + "/index.html";
            httpStatus = HttpStatus.FOUND;
        } catch (IllegalArgumentException e) {
            redirectTo = ControllerType.STATIC.getUrl() + "/user/join_failed.html";
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return ControllerResponse.builder()
                .httpStatus(httpStatus)
                .header(request.getHeader())
                .redirectTo(redirectTo)
                .build();
    }

    public ControllerResponse loginUser(ControllerRequest request) {
        String redirectTo;
        Map<String, String> header = request.getHeader();
        try {
            UserService.login(UserLoginRequest.mapToUserLoginRequest(request.getBody()));
            redirectTo = ControllerType.STATIC.getUrl() + "/index.html";
            header.put("Set-Cookie", "logined=true; Path=/");
        } catch (IllegalArgumentException e) {
            redirectTo = ControllerType.STATIC.getUrl() + "/user/login_failed.html";
            header.put("Set-Cookie", "logined=false");
        }

        return ControllerResponse.builder()
                .httpStatus(HttpStatus.FOUND)
                .header(header)
                .redirectTo(redirectTo)
                .build();
    }

}
