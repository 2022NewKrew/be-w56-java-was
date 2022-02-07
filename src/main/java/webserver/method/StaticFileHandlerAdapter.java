package webserver.method;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import webserver.HandlerAdapter;

import java.io.IOException;
import java.nio.file.Files;

public class StaticFileHandlerAdapter implements HandlerAdapter {
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
            e.printStackTrace();
        }
        return null;
    }
}
