package next.controller;

import webserver.context.HttpRequest;
import webserver.context.HttpResponse;

public interface Controller {
    void service(HttpRequest request, HttpResponse response);
}
