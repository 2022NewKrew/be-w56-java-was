package webserver;

import http.HttpRequestParser;
import http.render.ByteArrayBodyRenderer;
import http.render.EmptyBodyRenderer;
import http.render.HttpResponseRenderer;
import http.render.ResponseBodyRenderer;
import webserver.processor.HttpProcessor;
import webserver.processor.controller.Controller;
import webserver.processor.controller.StaticFileController;
import webserver.processor.controller.UserController;

import java.util.ArrayList;
import java.util.List;

public class HttpFactory {

    private static final HttpProcessor httpProcessor = new HttpProcessor(controllers());
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

    private static List<Controller> controllers() {
        List<Controller> controllers = new ArrayList<>();
        controllers.add(new StaticFileController());
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
