package webserver.controller;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class DefaultController implements Controller {
    @Override
    public HttpResponse handle(HttpRequest httpRequest) throws IOException {
        String url = httpRequest.getUrlPath();
        return HttpResponse.of(url, 200);
    }
}
