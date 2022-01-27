package framework;

import framework.handler.Handler;
import framework.handler.RequestMappingHandler;
import framework.handler.ResourceRequestHandler;
import util.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class HandlerMapping {

    // 쓸 수 있는 handler 모음
    private List<Handler> handlerMap = new ArrayList<>();


    public HandlerMapping() {
    }

    public void addHandler(Handler handler) {
        handlerMap.add(handler);
    }

    public Handler getHandler(HttpRequest httpRequest) {
        Handler mappedHandler = null;
        for (Handler handler : handlerMap) {
            if (handler.isSupport(httpRequest)) {
                mappedHandler = handler;
                break;
            }
        }
        return mappedHandler;
    }
}
