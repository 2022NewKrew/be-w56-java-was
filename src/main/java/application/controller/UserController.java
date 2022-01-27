package application.controller;

import application.service.UserService;
import common.controller.Controller;
import common.controller.ControllerResponse;
import common.controller.ControllerType;
import webserver.requesthandler.httprequest.HttpMethod;
import webserver.requesthandler.httpresponse.HttpStatus;

import java.util.Map;

public class UserController extends Controller {

    @Override
    public ControllerResponse doService(
            HttpMethod httpMethod,
            String url,
            Map<String, String> header,
            Map<String, String> requestBody
    ) {
        UserService.create(requestBody);
        HttpStatus httpStatus = HttpStatus.FOUND;
        header.put("Location", "http://localhost:8080/index.html");
        String redirectTo = ControllerType.STATIC.getUrl() + "/index.html";
        return new ControllerResponse(httpStatus, header, redirectTo);
    }
}
