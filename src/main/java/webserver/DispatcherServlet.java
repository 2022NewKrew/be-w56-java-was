package webserver;

import http.HttpRequest;
import http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.method.RequestMappingHandlerAdapter;
import webserver.method.RequestMappingHandlerMapping;
import webserver.method.StaticFileHandlerAdapter;
import webserver.method.StaticFileHandlerMapping;
import webserver.view.TemplateView;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DispatcherServlet {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
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
        ModelAndView mv = null;
        Exception exception = null;
        try {
            Object handler = getHandler(request);
            HandlerAdapter adapter = getHandlerAdapter(handler);
            mv = adapter.handle(request, handler);
        } catch (Exception e) {
            exception = e;
        }
        processDispatchResult(mv, out, exception);
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

    private void processDispatchResult(ModelAndView mv, OutputStream out, Exception exception) {
        if (exception != null || mv == null) {
            mv = new ModelAndView();
            mv.setStatus(HttpStatus.InternalServerError);
        }
        render(mv, out);
    }

    private void render(ModelAndView mv, OutputStream out) {
        View view = new TemplateView();
        view.render(mv, out);
    }
}
