package webserver.processor.handler;

import http.HttpRequest;
import http.HttpResponse;

public interface Handler {
    boolean supports(HttpRequest httpRequest);
    HttpResponse handle(HttpRequest httpRequest);
}
