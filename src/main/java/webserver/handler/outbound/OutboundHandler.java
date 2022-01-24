package webserver.handler.outbound;

import webserver.response.HttpResponse;

public abstract class OutboundHandler {

    public abstract void handle(HttpResponse response);
}
