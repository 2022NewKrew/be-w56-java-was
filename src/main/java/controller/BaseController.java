package controller;

import webserver.model.HttpRequest;

public interface BaseController {
    String process(HttpRequest request);
}
