package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;

public class FrontController {

    public static void getResponse(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {

        Controller controller = HandlerMapping.getController(httpRequest);

        String view = HandlerAdapter.getView(httpRequest, controller);

        ViewResolver.resolve(view, httpResponse);

    }
}
