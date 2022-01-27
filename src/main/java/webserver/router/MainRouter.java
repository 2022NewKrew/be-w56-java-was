package webserver.router;

import java.io.IOException;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class MainRouter {
    public static void routing(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        boolean isRouted = ControllerRouter.routing(httpRequest, httpResponse);

        if (!isRouted) {
            StaticRouter.routing(httpRequest, httpResponse);
        }
    }
}
