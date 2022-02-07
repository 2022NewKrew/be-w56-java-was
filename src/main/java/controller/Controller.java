package controller;

import network.HttpRequest;
import network.HttpResponse;

import java.io.IOException;

public interface Controller {
    HttpResponse response(HttpRequest httpRequest) throws IOException;
}
