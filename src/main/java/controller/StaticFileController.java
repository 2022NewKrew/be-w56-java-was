package controller;

import exception.NotFoundException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import java.util.Collections;
import java.util.HashMap;

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
                Collections.unmodifiableMap(new HashMap<>()),
                Collections.unmodifiableMap(new HashMap<>()),
                dos);
    }
}
