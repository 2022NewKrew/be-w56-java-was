package webserver.controller.common;

import util.request.HttpRequest;
import util.response.HttpResponse;
import webserver.controller.Controller;

import java.io.IOException;

public class ErrorController implements Controller<String> {
    @Override
    public boolean supports(HttpRequest httpRequest) {
        return true;
    }

    @Override
    public HttpResponse<String> handle(HttpRequest httpRequest) throws IOException {

        return null;
    }
}
