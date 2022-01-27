package webserver.handler;

import webserver.handler.inbound.InboundHandler;
import webserver.handler.outbound.ContentLengthHandler;
import webserver.handler.outbound.ContentTypeHandler;
import webserver.handler.outbound.OutboundHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton
 */
public class Handlers {

    private static Handlers INSTANCE;

    private Handlers() {
        createInboundHandlers();
        createOutboundHandlers();
    }

    public static Handlers getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Handlers();
        }
        return INSTANCE;
    }

    private List<InboundHandler> inboundHandlers;
    private List<OutboundHandler> outboundHandlers;

    private void createInboundHandlers() {
        List<InboundHandler> inboundHandlers = new ArrayList<>();
        this.inboundHandlers = inboundHandlers;
    }

    private void createOutboundHandlers() {
        List<OutboundHandler> outboundHandlers = new ArrayList<>();
        outboundHandlers.add(ContentLengthHandler.getInstance());
        outboundHandlers.add(ContentTypeHandler.getInstance());
        this.outboundHandlers = outboundHandlers;
    }

    public HttpRequest handleRequest(HttpRequest request) {
        inboundHandlers.stream()
                .forEach(inboundHandler -> inboundHandler.handle(request));
        return request;
    }

    public HttpResponse handleResponse(HttpResponse response) {
        outboundHandlers.stream()
                .forEach(outboundHandler -> outboundHandler.handle(response));
        return response;
    }
}
