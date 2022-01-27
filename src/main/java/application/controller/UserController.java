package application.controller;

import application.dto.UserCreateRequest;
import application.service.UserService;
import common.controller.AbstractController;
import common.controller.ControllerType;
import common.dto.ControllerRequest;
import common.dto.ControllerResponse;
import lombok.extern.slf4j.Slf4j;
import webserver.dto.response.HttpStatus;

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
        switch (request.getUrl()) {
            case "/users" -> createUser(UserCreateRequest.mapToUserCreateRequest(request.getBody()));
            case "/users/login" -> loginUser();
        }

        request.getHeader().put("Location", "http://localhost:8080/index.html");

        return ControllerResponse.builder()
                .httpStatus(HttpStatus.FOUND)
                .header(request.getHeader())
                .redirectTo(ControllerType.STATIC.getUrl() + "/index.html")
                .build();
    }

    public void createUser(UserCreateRequest userCreateRequest) {
        UserService.create(userCreateRequest);
    }

    public void loginUser() {
        // TODO
    }
}
