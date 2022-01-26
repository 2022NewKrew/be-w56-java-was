package webserver;

import java.io.IOException;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public interface MethodHandler {

    String handle(HttpRequest request, HttpResponse response) throws IOException, IllegalAccessException;
}
