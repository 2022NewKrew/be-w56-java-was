package webserver;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.http.HttpRequest;
import app.http.HttpResponse;
import webserver.handler.handlerAdapter.DefaultHandlerAdapter;
import webserver.handler.handlerAdapter.StaticHandlerAdapter;
import webserver.handler.handlerMapping.DefaultMapping;
import webserver.handler.handlerAdapter.HandlerAdapter;
import webserver.handler.handlerMapping.HandlerMapping;
import webserver.handler.HandlerMethod;

public class DispatcherServlet{
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;
    private static final DispatcherServlet dispatcherServlet = new DispatcherServlet();
    protected static DispatcherServlet getInstance() {
        return dispatcherServlet;
    }

    private DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
        this.handlerAdapters = new ArrayList<>();
        init();
    }

    private void init() {
        handlerMappings.add(new DefaultMapping());
        handlerAdapters.add(new DefaultHandlerAdapter());
        handlerAdapters.add(new StaticHandlerAdapter());
    }

    protected void doService(HttpRequest request, HttpResponse response) {
        HandlerMethod handlerMethod = getHandler(request);
        HandlerAdapter ha = getHandlerAdapter(handlerMethod);
        if(ha != null) {
            ha.handle(request, response, handlerMethod);
        }
    }

    private HandlerMethod getHandler(HttpRequest request){
        if (this.handlerMappings == null) {
            return null;
        }
        for (HandlerMapping mapping : this.handlerMappings) {
            HandlerMethod handlerMethod = mapping.getHandlerMethod(request);
            if (handlerMethod != null) {
                return handlerMethod;
            }
        }

        return null;
    }

    private HandlerAdapter getHandlerAdapter(HandlerMethod handlerMethod){
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if(handlerAdapter.supports(handlerMethod)) {
                log.debug("getHandlerAdpater: {}", handlerMethod==null);
                return handlerAdapter;
            }
        }
        return null;
    }
}
