package controller;

import http.request.HttpRequest;
import http.request.HttpRequestLine;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MainController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private static MainController INSTANCE;
    private final Map<String, Function<HttpRequest, HttpResponse>> methodMap = new HashMap<>();

    {
        methodMap.put("GET /", this::index);
    }

    private MainController() {
    }

    public static synchronized MainController getInstance() {
        if (INSTANCE == null)
            INSTANCE = new MainController();
        return INSTANCE;
    }

    @Override
    public HttpResponse processDynamic(HttpRequest request) throws IOException {
        final HttpRequestLine requestLine = request.line();
        log.debug("{} {}", requestLine.method(), requestLine.url());

        return null; // TODO
    }

    private HttpResponse index(HttpRequest request) {
        return redirect("index.html");
    }
}
