package controller;

import http.request.HttpRequest;
import java.util.Map;

public interface Controller {

    Map<String, String> run(HttpRequest request, Map<String, String> model);
}
