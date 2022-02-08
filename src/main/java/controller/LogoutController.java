package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import java.util.Map;

public class LogoutController implements Controller {

    private static LogoutController instance;

    public static LogoutController getInstance() {
        if(instance == null) {
            instance = new LogoutController();
        }
        return instance;
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
