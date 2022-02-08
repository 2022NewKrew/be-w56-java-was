package webserver.view;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.util.Map;

public interface View {
    void render(Map<String, Object> model, HttpRequest request, HttpResponse response);
}
