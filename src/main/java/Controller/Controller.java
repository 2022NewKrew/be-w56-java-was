package Controller;

import java.io.IOException;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public interface Controller {

    String process(HttpRequest request, HttpResponse response) throws IOException;
}
