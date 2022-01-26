package webserver;

import webserver.dispatcher.dynamic.DynamicDispatcher;
import webserver.dispatcher.sta.StaticDispatcher;
import webserver.request.HttpRequestUri;
import webserver.request.RequestContext;
import webserver.response.HttpResponse;

/**
 * Singleton
 */
public class ResponseProcessor {

    private static ResponseProcessor INSTANCE = new ResponseProcessor();

    private ResponseProcessor() {
    }

    public static ResponseProcessor getInstance() {
        return INSTANCE;
    }

    public HttpResponse response() {
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
