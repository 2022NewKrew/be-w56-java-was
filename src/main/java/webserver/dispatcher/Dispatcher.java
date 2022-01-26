package webserver.dispatcher;

import webserver.handler.Handlers;
import webserver.response.HttpResponse;

public abstract class Dispatcher {

    public HttpResponse dispatch() {
        HttpResponse response = processRequest();
        Handlers.getInstance().handleResponse(response);
        return response;
    }

    protected abstract HttpResponse processRequest();
}
