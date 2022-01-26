package webserver;

import http.parser.HttpRequestParser;
import http.render.ByteArrayBodyRenderer;
import http.render.EmptyBodyRenderer;
import http.render.HttpResponseRenderer;
import http.render.ResponseBodyRenderer;
import webserver.processor.HttpProcessor;
import webserver.processor.handler.Handler;
import webserver.processor.handler.controller.HandlerControllerMappingAdapter;
import webserver.processor.handler.StaticFileHandler;
import webserver.processor.handler.controller.Controller;
import webserver.processor.handler.controller.UserController;

import java.util.ArrayList;
import java.util.List;

public class HttpFactory {

    private static final HttpProcessor httpProcessor = new HttpProcessor(handlers());
    private static final HttpResponseRenderer httpResponseRenderer = new HttpResponseRenderer(responseBodyRenderers());
    private static final HttpRequestParser httpRequestParser = new HttpRequestParser();

    public static HttpProcessor httpProcessor() {
        return httpProcessor;
    }

    public static HttpRequestParser httpRequestParser() {
        return httpRequestParser;
    }

    public static HttpResponseRenderer httpResponseRenderer() {
        return httpResponseRenderer;
    }

    private static List<Handler> handlers() {
        List<Handler> handlers = new ArrayList<>();
        handlers.add(new StaticFileHandler());
        handlers.add(new HandlerControllerMappingAdapter(controllers()));
        return handlers;
    }

    private static List<Controller> controllers() {
        List<Controller> controllers = new ArrayList<>();
        controllers.add(new UserController());
        return controllers;
    }

    private static List<ResponseBodyRenderer> responseBodyRenderers() {
        List<ResponseBodyRenderer> responseBodyRenderers = new ArrayList<>();
        responseBodyRenderers.add(new ByteArrayBodyRenderer());
        responseBodyRenderers.add(new EmptyBodyRenderer());
        return responseBodyRenderers;
    }
}
