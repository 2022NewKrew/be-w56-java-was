package webserver.controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface HttpController {
    HttpResponse process(HttpRequest request);
}
