package webserver.custom;

import webserver.Dispatcher;
import webserver.context.Request;

import java.io.OutputStream;

public class DispatcherServlet implements Dispatcher {

    private final HandlerMapping handlerMapping;

    public DispatcherServlet(HandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    public void dispatch(Request request, OutputStream out) {

    }
}
