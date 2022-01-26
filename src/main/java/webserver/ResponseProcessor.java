package webserver;

import webserver.dispatcher.dynamic.DynamicDispatcher;
import webserver.dispatcher.sta.StaticDispatcher;
import webserver.request.HttpRequestUri;
import webserver.request.RequestContext;
import webserver.response.HttpResponse;
import webserver.response.HttpStatus;
import webserver.response.ResponseContext;

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

    public HttpResponse process() {
        HttpResponse response = ResponseContext.getInstance().createResponse();
        try {
            processRequestByDispatcher();
        } catch (RuntimeException e) {
            setResponseForInternalServerError(response);
        }

        return response;
    }

    private HttpResponse processRequestByDispatcher() {
        HttpRequestUri uri = RequestContext.getInstance().getHttpRequest().getUri();
        if (uri.isForStaticContent()) {
            return processForStaticContent();
        }
        return processForDynamicContent();
    }

    private HttpResponse processForStaticContent() {
        return StaticDispatcher.getInstance().dispatch();
    }

    private HttpResponse processForDynamicContent() {
        return DynamicDispatcher.getInstance().dispatch();
    }

    private void setResponseForInternalServerError(HttpResponse response) {
        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setResponseBody(new byte[]{});
    }
}
