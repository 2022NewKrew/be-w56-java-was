package webserver.processor;

import http.HttpRequest;
import http.HttpResponse;
import webserver.exception.InternalServerErrorException;
import webserver.exception.ResourceNotFoundException;
import webserver.processor.exception.ExceptionResolver;
import webserver.processor.handler.Handler;

import java.util.List;

public class HttpProcessor {

    private final List<Handler> handlers;
    private final List<ExceptionResolver> exceptionResolvers;

    public HttpProcessor(List<Handler> handlers, List<ExceptionResolver> exceptionResolvers) {
        this.handlers = handlers;
        this.exceptionResolvers = exceptionResolvers;
    }

    public HttpResponse process(HttpRequest httpRequest) throws Exception {
        HttpResponse httpResponse = null;
        try {
            Handler handler = findHandler(httpRequest);
            httpResponse = handler.handle(httpRequest);
        } catch (Exception e) {
            ExceptionResolver resolver = findExceptionResolver(e);
            checkResolver(resolver, e);
            httpResponse = resolver.resolve(e);
        }
        return httpResponse;
    }

    private void checkResolver(ExceptionResolver exceptionResolver, Exception e) throws Exception {
        if(exceptionResolver == null) {
            throw e;
        }
    }

    private ExceptionResolver findExceptionResolver(Exception e) {
        for(ExceptionResolver resolver : exceptionResolvers) {
            if(resolver.supports(e)) {
                return resolver;
            }
        }
        return null;
    }

    private Handler findHandler(HttpRequest httpRequest) {
        for(Handler handler : handlers) {
            if(handler.supports(httpRequest)) {
                return handler;
            }
        }
        throw new ResourceNotFoundException("");
    }
}
