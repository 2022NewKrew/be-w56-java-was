package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpResponseFactory;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> result = new HashMap<>();
        result.put("url", request.getUrl());
        result.put("status", "200");

        return HttpResponseFactory.getHttpResponse(result, new HashMap<>(), dos);
    }
}
