package webserver;

import http.parser.HttpRequestParser;
import http.render.HttpResponseRenderer;
import webserver.file.DocumentRoot;
import webserver.http.HttpEntityConverter;
import webserver.http.request.ByteArrayRequestBodyResolver;
import webserver.http.request.JsonRequestBodyResolver;
import webserver.http.request.RequestBodyResolver;
import webserver.http.request.StringRequestBodyResolver;
import webserver.processor.HttpProcessor;
import webserver.processor.exception.ExceptionResolver;
import webserver.processor.exception.ResourceNotFoundExceptionResolver;
import webserver.processor.handler.Handler;
import webserver.processor.handler.controller.HandlerControllerMappingAdapter;
import webserver.processor.handler.StaticFileHandler;
import webserver.processor.handler.controller.Controller;

import java.util.ArrayList;
import java.util.List;

public class HttpFactory {

    private static HttpProcessor httpProcessor;
    private static HttpResponseRenderer httpResponseRenderer;
    private static HttpRequestParser httpRequestParser;
    private static HttpEntityConverter httpEntityConverter;
    private static DocumentRoot documentRoot;

    public static void initialize(List<Controller> controllers) {
        documentRoot = new DocumentRoot();
        httpEntityConverter = new HttpEntityConverter(requestBodyResolvers());
        httpProcessor = new HttpProcessor(handlers(controllers), exceptionResolvers());
        httpResponseRenderer = new HttpResponseRenderer();
        httpRequestParser = new HttpRequestParser();
    }

    public static HttpProcessor httpProcessor() {
        return httpProcessor;
    }

    public static HttpRequestParser httpRequestParser() {
        return httpRequestParser;
    }

    public static HttpResponseRenderer httpResponseRenderer() {
        return httpResponseRenderer;
    }

    public static HttpEntityConverter httpEntityConverter() {
        return httpEntityConverter;
    }

    public static DocumentRoot documentRoot() {
        return documentRoot;
    }

    private static List<Handler> handlers(List<Controller> controllers) {
        List<Handler> handlers = new ArrayList<>();
        handlers.add(new StaticFileHandler());
        handlers.add(handlerControllerMappingAdapter(controllers));
        return handlers;
    }

    private static HandlerControllerMappingAdapter handlerControllerMappingAdapter(List<Controller> controllers) {
        return new HandlerControllerMappingAdapter(controllers);
    }

    private static List<RequestBodyResolver> requestBodyResolvers() {
        List<RequestBodyResolver> requestBodyResolvers = new ArrayList<>();
        requestBodyResolvers.add(new StringRequestBodyResolver());
        requestBodyResolvers.add(new JsonRequestBodyResolver());
        requestBodyResolvers.add(new ByteArrayRequestBodyResolver());
        return requestBodyResolvers;
    }

    private static List<ExceptionResolver> exceptionResolvers() {
        List<ExceptionResolver> exceptionResolvers = new ArrayList<>();
        exceptionResolvers.add(new ResourceNotFoundExceptionResolver(documentRoot()));
        return exceptionResolvers;
    }
}
