package framework.view;

import framework.http.HttpRequest;
import framework.http.HttpResponse;

import java.util.Map;

public interface View {
    void render(Map<String, Object> model, HttpRequest request, HttpResponse response);
}
