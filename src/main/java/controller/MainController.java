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
        String[] urlTokens = requestLine.url().split("/");
        String methodAndUrl = requestLine.method() + " /";
        if (urlTokens.length > 0) {
            String urlWithoutQueryString = urlTokens[urlTokens.length - 1].split("\\?")[0];
            methodAndUrl += urlWithoutQueryString;
        }

        log.debug("{} {}", requestLine.method(), requestLine.url());
        if (methodMap.containsKey(methodAndUrl)) {
            log.debug("{} called", methodAndUrl);
            return methodMap.get(methodAndUrl).apply(request);
        } else {
            log.debug("{} {}, redirect to error page", requestLine.method(), requestLine.url());
            return errorPage();
        }
    }

    private HttpResponse index(HttpRequest request) {
        if ("true".equals(request.header().getCookie("logined"))) {
            log.debug("logined user"); // TODO
        }

        log.debug("{}, redirect to index", request.line().url());
        return redirect("index.html");
    }
}
