package webserver.processor.controller;

import http.*;
import webserver.file.DocumentRoot;

import java.net.URI;
import java.util.Map;

public class StaticFileController implements Controller {

    private final DocumentRoot documentRoot = new DocumentRoot();

    @Override
    public boolean supports(HttpRequest httpRequest) {
        String requestPath = httpRequest.getPath();
        return httpRequest.getMethod().equals(HttpMethod.GET) && documentRoot.existsFile(requestPath);
    }

    @Override
    public HttpResponse process(HttpRequest httpRequest) {
        URI requestUri = httpRequest.getRequestUri();
        byte[] responseBody = documentRoot.readFileByPath(requestUri.getPath());
        return new HttpResponse(new HttpHeaders(Map.of()), StatusCode.OK, responseBody);
    }
}
