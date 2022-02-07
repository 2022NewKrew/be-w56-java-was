package webserver;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import webserver.method.RequestMappingHandlerAdapter;
import webserver.method.RequestMappingHandlerMapping;
import webserver.method.StaticFileHandlerAdapter;
import webserver.method.StaticFileHandlerMapping;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DispatcherServlet {
    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public void initialize() {
        initHandlerMappings();
        initHandlerAdapters();
    }

    private void initHandlerMappings() {
        handlerMappings = new ArrayList<>();
        RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.initHandlerMethods();
        handlerMappings.add(requestMappingHandlerMapping);
        StaticFileHandlerMapping staticFileHandlerMapping = new StaticFileHandlerMapping();
        staticFileHandlerMapping.initHandlerMethods();
        handlerMappings.add(staticFileHandlerMapping);
    }

    private void initHandlerAdapters() {
        handlerAdapters = new ArrayList<>();
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapters.add(requestMappingHandlerAdapter);
        StaticFileHandlerAdapter staticFileHandlerAdapter = new StaticFileHandlerAdapter();
        handlerAdapters.add(staticFileHandlerAdapter);
    }

    public void doDispatch(HttpRequest request, OutputStream out) {
        HttpResponse response = null;
        Exception exception = null;
        try {
            Object handler = getHandler(request);
            HandlerAdapter adapter = getHandlerAdapter(handler);
            response = adapter.handle(request, handler);
        } catch (Exception e) {
            exception = e;
        }
        processDispatchResult(response, out, exception);
    }

    private Object getHandler(HttpRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No adapter for handler [" + handler + "]"));
    }

    private void processDispatchResult(HttpResponse response, OutputStream out, Exception exception) {
        if (response == null) {
            response = new HttpResponse(HttpStatus.InternalServerError);
        }
        try {
            response.render(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
