package webserver;

import http.HttpHandler;
import http.HttpRequestParser;
import http.HttpResponseRenderer;
import webserver.file.DocumentRoot;
import webserver.http.HttpEntityConverter;
import webserver.http.request.EmptyBodyResolver;
import webserver.http.request.RequestBodyResolver;
import webserver.http.request.StringRequestBodyResolver;
import webserver.http.response.RedirectResolver;
import webserver.http.response.ResponseBodyResolver;
import webserver.http.response.ViewResolver;
import webserver.processor.HttpProcessor;
import webserver.http.exception.ExceptionResolver;
import webserver.http.exception.ResourceNotFoundExceptionResolver;
import webserver.processor.handler.controller.ControllerMappingAdapterHandler;
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

    public static void initialize(List<Controller> controllers, List<ExceptionResolver> exceptionResolvers) {
        documentRoot = new DocumentRoot();
        httpEntityConverter = new HttpEntityConverter(requestBodyResolvers(), responseBodyResolvers());
        httpProcessor = new HttpProcessor(handlers(controllers, exceptionResolvers));
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

    private static List<HttpHandler> handlers(List<Controller> controllers, List<ExceptionResolver> customExceptionResolvers) {
        List<HttpHandler> handlers = new ArrayList<>();
        handlers.add(new StaticFileHandler());
        handlers.add(handlerControllerMappingAdapter(controllers, customExceptionResolvers));
        return handlers;
    }

    private static ControllerMappingAdapterHandler handlerControllerMappingAdapter(List<Controller> controllers, List<ExceptionResolver> customExceptionResolvers) {
        return new ControllerMappingAdapterHandler(controllers, exceptionResolvers(customExceptionResolvers));
    }

    private static List<RequestBodyResolver> requestBodyResolvers() {
        List<RequestBodyResolver> requestBodyResolvers = new ArrayList<>();
        requestBodyResolvers.add(new StringRequestBodyResolver());
        requestBodyResolvers.add(new EmptyBodyResolver());
        return requestBodyResolvers;
    }

    private static List<ResponseBodyResolver> responseBodyResolvers() {
        List<ResponseBodyResolver> responseBodyResolvers = new ArrayList<>();
        responseBodyResolvers.add(new ViewResolver(documentRoot()));
        responseBodyResolvers.add(new RedirectResolver());
        return responseBodyResolvers;
    }

    private static List<ExceptionResolver> exceptionResolvers(List<ExceptionResolver> customResolvers) {
        List<ExceptionResolver> exceptionResolvers = new ArrayList<>(customResolvers);
        exceptionResolvers.add(new ResourceNotFoundExceptionResolver());
        return exceptionResolvers;
    }
}
