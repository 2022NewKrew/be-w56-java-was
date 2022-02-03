package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import util.ControllerUtils;

public class RootController implements Controller{

    private static RootController instance;

    public static synchronized RootController getInstance() {
        if(instance == null) {
            instance = new RootController();
        }
        return instance;
    }

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) {
        return HttpResponse.found(
                "/index.html",
                ControllerUtils.getEmptyImmutableMap(),
                dos
        );
    }
}
