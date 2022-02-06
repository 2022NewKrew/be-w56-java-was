package webserver.servlet.method;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.servlet.HttpControllable;
import webserver.servlet.HttpHandleable;
import webserver.servlet.RequestMapping;

public class PostHandler implements HttpHandleable {

    private final RequestMapping requestMapping;

    public PostHandler(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        HttpControllable controller = this.requestMapping.getHandler(request.getUri());
        controller.call(request, response);
    }
}
