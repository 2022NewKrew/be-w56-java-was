package controller;

import util.HttpRequest;
import util.HttpResponse;

import java.io.IOException;

public interface Router {
    void send(HttpRequest request, HttpResponse response) throws IOException;
}
