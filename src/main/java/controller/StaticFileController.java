package controller;

import http.request.HttpRequest;
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
    public Map<String, String> run(HttpRequest request, Map<String, String> model) {
        Map<String, String> result = new HashMap<>();
        result.put("url", request.getUrl());
        result.put("status", "200");
        return result;
    }
}
