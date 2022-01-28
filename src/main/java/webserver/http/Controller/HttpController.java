package webserver.http.Controller;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;

public interface HttpController {
    boolean isValidRequest(HttpRequest request);

    HttpResponse handleRequest(HttpRequest request, OutputStream out) throws IOException;
}
