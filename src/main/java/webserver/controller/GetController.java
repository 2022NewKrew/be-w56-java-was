package webserver.controller;

import network.HttpRequest;
import network.HttpResponse;
import webserver.service.UserService;
import java.io.OutputStream;

public class GetController {

    private HttpResponse httpResponse;
    private UserService userService;

    public GetController(HttpRequest httpRequest, OutputStream outputStream) {
        this.httpResponse = new HttpResponse(outputStream);
        this.userService = new UserService(httpRequest);
    }

    public void handleGet(String path) {
        httpResponse.setPath(path);

        if (path.contains("css")) {
            httpResponse.ok("css");
            return;
        }
        httpResponse.ok("html");
    }
}
