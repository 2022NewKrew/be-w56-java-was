package webserver.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import template.Model;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class HandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(HandlerAdapter.class);

    private HandlerAdapter() {

    }

    public static HandlerAdapter getInstance() {
        return HandlerAdapterHolder.INSTANCE;
    }

    public String handle(HttpRequest request, HttpResponse response, Model model) {
        HandlerMapping handlerMapping = HandlerMapping.getInstance();

        if (!handlerMapping.hasMatchedHandler(request.getUri())) {
            return request.getUri();
        }
        HttpControllable controller = handlerMapping.getHandler(request.getUri());
        logger.info("[call] : " + controller.getClass());
        return controller.call(request, response, model);
    }

    private static class HandlerAdapterHolder {

        public static HandlerAdapter INSTANCE = new HandlerAdapter();

    }

}
