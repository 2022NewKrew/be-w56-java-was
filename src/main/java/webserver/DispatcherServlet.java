package webserver;

import static util.HttpRequestUtils.parseRedirect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.http.HttpRequest;
import app.http.HttpResponse;
import webserver.handler.DefaultMapping;
import webserver.handler.DefaultMappingHandlerAdapter;
import webserver.handler.HandlerAdapter;
import webserver.handler.HandlerMapping;
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
        handlerAdapters.add(new DefaultMappingHandlerAdapter());
    }

    protected HttpResponse doService(HttpRequest request) {
        HandlerMethod handlerMethod = getHandler(request);
        if(handlerMethod == null) {
            return request.makeResponse();
        }
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if(handlerAdapter.supports(handlerMethod)) {
                Method method = handlerMethod.getMethod();
                String handlerResponse = handlerAdapter.handle(request, handlerMethod);
                return getHttpResponse(request, handlerResponse);
            }
        }
        return null;
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

    private HttpResponse getHttpResponse(HttpRequest request, String handlerResponse) {
        return request.makeResponse(parseRedirect(handlerResponse));
    }
}
