package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import util.ControllerUtils;

public class StaticFileController implements Controller {

    private static StaticFileController instance;

    public static synchronized StaticFileController getInstance() {
        if (instance == null) {
            instance = new StaticFileController();
        }
        return instance;
    }

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) {
        return HttpResponse.ok(
                request.getUrl(),
                ControllerUtils.getEmptyImmutableMap(),
                ControllerUtils.getEmptyImmutableMap(),
                dos);
    }
}
