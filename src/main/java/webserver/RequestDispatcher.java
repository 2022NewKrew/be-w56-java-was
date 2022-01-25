package webserver;

import org.slf4j.LoggerFactory;

import java.net.Socket;
import org.slf4j.Logger;
import webserver.handler.Handler;

public class RequestDispatcher {

    public static Logger log = LoggerFactory.getLogger(RequestDispatcher.class);

    public HandlerMapping handlerMapping = new HandlerMapping();

    public void doDispatch(Socket connection) {
        HttpRequest httpRequest = new HttpRequest(connection);
        Handler mappedHandler = handlerMapping.getHandler(httpRequest);
        mappedHandler.handle(httpRequest);
    }
}
