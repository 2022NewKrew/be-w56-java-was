package app;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.servlet.HttpControllable;

public class RootController implements HttpControllable {

    public static final String path = "/";

    public RootController() {
    }

    @Override
    public void call(HttpRequest request, HttpResponse response) {
        
    }
}
