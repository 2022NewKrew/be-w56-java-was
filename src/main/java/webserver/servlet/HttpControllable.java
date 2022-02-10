package webserver.servlet;

import template.Model;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface HttpControllable {

    String call(HttpRequest request, HttpResponse response, Model model);
}
