package webserver.http.request.handler;

import java.io.IOException;
import webserver.http.request.exceptions.PageNotFoundException;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public interface MethodHandler {

    void handle(HttpRequest request, HttpResponse response)
        throws IOException, PageNotFoundException;
}
