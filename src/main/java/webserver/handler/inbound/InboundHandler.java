package webserver.handler.inbound;

import webserver.request.HttpRequest;

public abstract class InboundHandler {

    public abstract void handle(HttpRequest request);
}
