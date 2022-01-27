package webserver.processor.handler;

import http.*;
import webserver.HttpFactory;
import webserver.file.DocumentRoot;

import java.net.URI;
import java.util.Map;

public class StaticFileHandler implements Handler {

    @Override
    public boolean supports(HttpRequest httpRequest) {
        DocumentRoot documentRoot = HttpFactory.documentRoot();
        String requestPath = httpRequest.getPath();
        return httpRequest.getMethod().equals(HttpMethod.GET) && documentRoot.existsFile(requestPath);
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        DocumentRoot documentRoot = HttpFactory.documentRoot();
        URI requestUri = httpRequest.getRequestUri();
        byte[] responseBody = documentRoot.readFileByPath(requestUri.getPath());
        return new HttpResponse(new HttpHeaders(Map.of()), StatusCode.OK, responseBody);
    }
}
