package webserver.controller.user;

import util.request.HttpRequest;
import util.response.HttpResponse;
import webserver.controller.Controller;

import java.io.IOException;

public class UserLoginController implements Controller<String> {
    @Override
    public boolean supports(HttpRequest httpRequest) {
        return false;
    }

    @Override
    public HttpResponse<String> handle(HttpRequest httpRequest) throws IOException {
        return null;
    }
}
