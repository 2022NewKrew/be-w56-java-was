package application.controller;

import application.dto.UserCreateRequest;
import application.service.UserService;
import common.controller.Controller;
import common.controller.ControllerResponse;
import common.controller.ControllerType;
import webserver.dto.request.HttpMethod;
import webserver.dto.response.HttpStatus;

import java.util.Map;

public class UserController extends Controller {

    @Override
    public ControllerResponse doService(
            HttpMethod httpMethod,
            String url,
            Map<String, String> header,
            Map<String, String> requestBody
    ) {
        UserService.create(UserCreateRequest.mapToUserCreateRequest(requestBody));
        HttpStatus httpStatus = HttpStatus.FOUND;
        header.put("Location", "http://localhost:8080/index.html");
        String redirectTo = ControllerType.STATIC.getUrl() + "/index.html";
        return new ControllerResponse(httpStatus, header, redirectTo);
    }
}
