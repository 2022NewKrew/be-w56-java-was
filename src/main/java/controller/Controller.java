package controller;

import model.Request;
import model.Response;

import java.io.IOException;

public interface Controller {
    void run(Request request, Response response) throws IOException;
}
