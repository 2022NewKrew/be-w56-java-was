package controller;

import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

public interface BaseController {
    HttpResponse process(HttpRequest request);
}
