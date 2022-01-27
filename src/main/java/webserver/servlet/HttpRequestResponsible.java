package webserver.servlet;

import java.io.IOException;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface HttpRequestResponsible {

    HttpResponse handle(HttpRequest request, HttpResponse response) throws IOException;

}
