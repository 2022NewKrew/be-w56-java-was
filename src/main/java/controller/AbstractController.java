package controller;

import model.Request;
import model.Response;

import java.io.IOException;

public abstract class AbstractController implements Controller {
    @Override
    public void run(Request request, Response response) throws IOException {
        if (request.getRequestMethod().equals("GET")) {
            doGet(request, response);
        }

        if (request.getRequestMethod().equals("POST")) {
            doPost(request, response);
        }
    }

    protected abstract void doGet(Request request, Response response) throws IOException;
    protected abstract void doPost(Request request, Response response) throws IOException;
}
