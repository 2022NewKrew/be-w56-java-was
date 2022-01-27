package webserver.controller;

import common.controller.Controller;
import common.controller.ControllerResponse;
import common.controller.ControllerType;
import webserver.dto.request.HttpMethod;
import webserver.dto.response.HttpStatus;

import java.util.Map;

public class StaticResourceController extends Controller {

    @Override
    public ControllerResponse doService(
            HttpMethod httpMethod,
            String url,
            Map<String, String> header,
            Map<String, String> requestBody
    ) {
        HttpStatus httpStatus = HttpStatus.OK;
        String redirectTo = ControllerType.STATIC.getUrl() + url;

        return new ControllerResponse(httpStatus, header, redirectTo);
    }
}
