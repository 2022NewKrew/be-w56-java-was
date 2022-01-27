package webserver.controller;

import common.controller.Controller;
import common.controller.ControllerResponse;
import common.controller.ControllerType;
import webserver.requesthandler.httprequest.HttpMethod;
import webserver.requesthandler.httpresponse.HttpStatus;

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
