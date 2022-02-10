package webserver.router;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import webserver.adapter.HandlerAdapter;
import webserver.adapter.StaticHandlerAdapter;
import webserver.adapter.UrlHandlerAdapter;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class MainRouter {
    private static final List<HandlerAdapter> HANDLER_ADAPTER_LIST;

    static {
        HANDLER_ADAPTER_LIST = Arrays.asList(new UrlHandlerAdapter(), new StaticHandlerAdapter());
    }

    public static void route(HttpRequest request, HttpResponse response) throws IOException {
        for (HandlerAdapter handlerAdapter : HANDLER_ADAPTER_LIST) {
            if (handlerAdapter.isSupport(request)) {
                handlerAdapter.handle(request, response);
                return;
            }
        }
    }
}
