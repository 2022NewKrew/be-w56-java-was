package webserver.adapter;

import java.io.IOException;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface HandlerAdapter {
    boolean isSupport(HttpRequest request);

    void handle(HttpRequest request, HttpResponse response) throws IOException;
}
