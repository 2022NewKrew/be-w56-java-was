package webserver.processor.exception;

import http.HttpHeaders;
import http.HttpResponse;
import http.StatusCode;
import webserver.exception.ResourceNotFoundException;
import webserver.file.DocumentRoot;

import java.util.Map;

public class ResourceNotFoundExceptionResolver implements ExceptionResolver {

    private final DocumentRoot documentRoot;

    public ResourceNotFoundExceptionResolver(DocumentRoot documentRoot) {
        this.documentRoot = documentRoot;
    }

    @Override
    public boolean supports(Exception e) {
        return e.getClass().isAssignableFrom(ResourceNotFoundException.class);
    }

    @Override
    public HttpResponse resolve(Exception e) {
        HttpHeaders headers = new HttpHeaders(Map.of());
        byte[] body = documentRoot.readFileByPath("/404.html");
        return new HttpResponse(headers, StatusCode.NOT_FOUND, body);
    }
}
