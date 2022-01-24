package webserver;

import webserver.dispatcher.DynamicDispatcher;
import webserver.dispatcher.StaticDispatcher;
import webserver.request.HttpRequest;
import webserver.request.HttpRequestUri;
import webserver.request.RequestContext;
import webserver.response.HttpResponse;

/**
 * Singleton
 */
public class ResponseProcessor {

    private static ResponseProcessor INSTANCE;

    private ResponseProcessor() {
    }

    public static ResponseProcessor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ResponseProcessor();
        }
        return INSTANCE;
    }

    public HttpResponse response(HttpRequest request) {
        HttpRequestUri uri = RequestContext.getInstance().getHttpRequest().getUri();
        if (uri.isForStaticContent()) {
            return getResponseForStaticContent();
        }
        return getResponseForDynamicContent();
    }

    private HttpResponse getResponseForStaticContent() {
        return StaticDispatcher.getInstance().getResponse();
    }

    private HttpResponse getResponseForDynamicContent() {
        return DynamicDispatcher.getInstance().getResponse();
    }

}
