package webserver.method;

import http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HandlerAdapter;
import webserver.ModelAndView;

public class StaticFileHandlerAdapter implements HandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(StaticFileHandlerAdapter.class);

    @Override
    public boolean supports(Object handler) {
        return handler instanceof StaticFileHandler;
    }

    @Override
    public ModelAndView handle(HttpRequest request, Object handler) {
        StaticFileHandler fileHandler = (StaticFileHandler) handler;
        return new ModelAndView(fileHandler.getPath());
    }
}
