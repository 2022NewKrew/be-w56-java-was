package webserver.dispatcher;

import webserver.handler.Handlers;
import webserver.response.HttpResponse;

public abstract class Dispatcher {

    public HttpResponse getResponse() {
        HttpResponse response = createResponse();
        Handlers.getInstance().handleResponse(response);
        return response;
    }

    protected abstract HttpResponse createResponse();
}
