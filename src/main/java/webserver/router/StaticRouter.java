package webserver.router;

import java.io.IOException;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class StaticRouter {
    public static void routing(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.send(httpRequest.getUrl(), httpRequest.getContentType());
    }
}
