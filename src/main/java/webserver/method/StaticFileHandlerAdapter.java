package webserver.method;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HandlerAdapter;

import java.io.IOException;
import java.nio.file.Files;

public class StaticFileHandlerAdapter implements HandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(StaticFileHandlerAdapter.class);

    @Override
    public boolean supports(Object handler) {
        return handler instanceof StaticFileHandler;
    }

    @Override
    public HttpResponse handle(HttpRequest request, Object handler) {
        StaticFileHandler fileHandler = (StaticFileHandler) handler;
        try {
            byte[] content = Files.readAllBytes(fileHandler.getPath());
            return new HttpResponse(content, HttpStatus.OK);
        } catch (IOException e) {
            log.error("Failed to read file of " + fileHandler.getPath());
        }
        return null;
    }
}
