package router;

import java.io.IOException;

import http.HttpRequest;
import http.HttpResponse;

public class MainRouter {
    public static void routing(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        switch (httpRequest.getMethod()) {
            case "GET":
                GetMappingRouter.routing(httpRequest, httpResponse);
                break;

            case "POST":
                //todo post routing 추가
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + httpRequest.getMethod());
        }
    }
}
