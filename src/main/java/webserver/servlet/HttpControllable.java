package webserver.servlet;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface HttpControllable {

    void call(HttpRequest request, HttpResponse response);
}
