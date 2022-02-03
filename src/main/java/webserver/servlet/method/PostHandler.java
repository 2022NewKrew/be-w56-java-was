package webserver.servlet.method;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.servlet.ApplicationContext;
import webserver.servlet.HttpControllable;
import webserver.servlet.HttpHandleable;

public class PostHandler implements HttpHandleable {

    private final ApplicationContext applicationContext;

    public PostHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        HttpControllable controller = this.applicationContext.getHandler(request.getUri());
        controller.call(request, response);
    }
}
