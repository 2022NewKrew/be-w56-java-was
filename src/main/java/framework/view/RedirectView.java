package framework.view;

import framework.http.HttpRequest;
import framework.http.HttpResponse;
import framework.http.HttpStatus;

import java.util.Map;

public class RedirectView implements View {
    private final String viewName;

    public RedirectView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, Object> model, HttpRequest request, HttpResponse response) {
        response.setStatus(HttpStatus.FOUND);
        response.setHeader("Location", viewName);
    }
}
