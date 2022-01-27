package webserver.servlet.method;

import java.io.IOException;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface MethodHandler {

    HttpResponse handle(HttpRequest request, HttpResponse response) throws IOException;
}
