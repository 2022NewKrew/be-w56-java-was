package controller;

import util.Request;
import util.Response;

import java.io.IOException;

public interface Router {
    void send(Request request, Response response) throws IOException;
}
