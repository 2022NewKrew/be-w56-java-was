package webserver.processor;

import http.HttpRequest;
import http.HttpResponse;
import webserver.exception.ResourceNotFoundException;
import webserver.processor.handler.Handler;

import java.util.List;

public class HttpProcessor {

    private final List<Handler> handlers;

    public HttpProcessor(List<Handler> handlers) {
        this.handlers = handlers;
    }

    public HttpResponse process(HttpRequest httpRequest) {
        for(Handler handler : handlers) {
            if(handler.supports(httpRequest)) {
                return handler.handle(httpRequest);
            }
        }
        throw new ResourceNotFoundException();
    }
}
