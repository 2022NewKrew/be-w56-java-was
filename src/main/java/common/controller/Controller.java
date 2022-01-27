package common.controller;

import webserver.requesthandler.httprequest.HttpMethod;

import java.util.Map;
public abstract class Controller {
    public abstract ControllerResponse doService(
            HttpMethod httpMethod,
            String url,
            Map<String, String> header,
            Map<String, String> requestBody
    );
}
