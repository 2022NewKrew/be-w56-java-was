package webserver;

import controller.adapter.HandlerAdapter;
import controller.adapter.StaticHandlerAdapter;
import controller.adapter.UrlMappingHandlerAdapter;
import http.request.HttpRequest;
import http.response.HttpResponse;

import java.util.Arrays;
import java.util.List;

public class Router {
    public static final String WEB_ROOT = "./webapp";
    private static final List<HandlerAdapter> adapters = Arrays.asList(new StaticHandlerAdapter(), new UrlMappingHandlerAdapter());

    public static void route(HttpRequest request, HttpResponse response) {
        for (HandlerAdapter adapter : adapters) {
            if (adapter.supports(request)) {
                adapter.handle(request, response);
                return;
            }
        }
    }
}
