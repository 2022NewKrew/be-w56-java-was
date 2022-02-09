package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import java.util.Map;

public class LogoutController implements Controller {

    private static final LogoutController INSTANCE = new LogoutController();

    public static LogoutController getInstance() {
        return INSTANCE;
    }

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) {
        return HttpResponse.found(
                "/",
                Map.of("logined", "false; Path=/; Expires=", "userId", "null; Path=/; Expire="),
                dos
        );
    }
}
