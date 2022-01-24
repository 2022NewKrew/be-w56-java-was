package webserver;

import http.HttpRequest;
import http.HttpResponse;
import http.impl.HttpFactory;
import webserver.file.DocumentRoot;

import java.net.URI;

public class HttpProcessor {

    private final DocumentRoot documentRoot = new DocumentRoot();

    public HttpResponse process(HttpRequest httpRequest) {
        URI requestUri = httpRequest.getRequestUri();
        byte[] responseBody = documentRoot.readFileByPath(requestUri);
        return HttpFactory.createHttpResponse(responseBody);
    }
}
