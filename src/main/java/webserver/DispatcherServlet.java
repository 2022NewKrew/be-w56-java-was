package webserver;

import static util.Constant.CONTENT_LENGTH;
import static util.Constant.CONTENT_TYPE;
import static util.Constant.ERROR_PATH;
import static util.Constant.UTF_8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.exception.CustomException;
import app.http.HttpRequest;
import app.http.HttpResponse;
import app.http.HttpStatus;
import app.http.HttpVersion;
import app.http.Mime;
import webserver.handler.HandlerMethod;
import webserver.handler.handlerAdapter.DefaultHandlerAdapter;
import webserver.handler.handlerAdapter.HandlerAdapter;
import webserver.handler.handlerAdapter.StaticHandlerAdapter;
import webserver.handler.handlerMapping.DefaultMapping;
import webserver.handler.handlerMapping.HandlerMapping;

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
        try {
            if (ha != null) {
                ha.handle(request, response, handlerMethod);
            }
        }catch(CustomException e) {
            log.error("CustomException" + e.getMessage());
            setResponseError(request, response, e.getStatus());
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
                return handlerAdapter;
            }
        }
        return null;
    }

    private void setResponseError(HttpRequest request, HttpResponse response, HttpStatus status) {
        try {
            String path = ERROR_PATH;
            response.put(CONTENT_TYPE, Mime.getMime(path).getContentType() + UTF_8);
            byte[] body = Files.readAllBytes(new File(path).toPath());
            response.put(CONTENT_LENGTH, String.valueOf(body.length));
            response.setVersion(HttpVersion.HTTP_1_1);
            response.setStatus(status);
            response.setBody(body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
