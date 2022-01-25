package webserver.controller;

import webserver.Request;
import webserver.Response;

import java.io.IOException;

public interface Controller {
    void start(Request request, Response response) throws IOException;
}
