package controller;

import webserver.HttpRequest;

public interface BaseController {
    String process(HttpRequest request);
}
