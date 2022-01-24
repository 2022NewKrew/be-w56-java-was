package webserver;

import webserver.handler.Handler;
import webserver.handler.ResourceRequestHandler;

import java.util.ArrayList;
import java.util.List;

public class HandlerMapping {

    // 쓸 수 있는 handler 모음
    private List<Handler> handlerMap = new ArrayList<>();

    public HandlerMapping() {
        handlerMap.add(new ResourceRequestHandler());
    }

    public Handler getHandler(HttpRequest httpRequest) {
        Handler mappedHandler = null;
        for (Handler handler : handlerMap) {
            if (handler.isSupport(httpRequest))
                mappedHandler = handler;
                break;
        }
        return mappedHandler;
    }
}
