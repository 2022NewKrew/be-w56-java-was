package controller;

import httpmodel.HttpMethod;
import httpmodel.HttpRequest;
import httpmodel.HttpResponse;
import java.io.IOException;

public abstract class AbstractController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        if (request.isEqualsHttpMethod(HttpMethod.GET)) {
            doGet(request, response);
        }
        if (request.isEqualsHttpMethod(HttpMethod.POST)) {
            doPost(request, response);
        }
    }

    protected abstract void doGet(HttpRequest request, HttpResponse response) throws IOException;

    protected abstract void doPost(HttpRequest request, HttpResponse response) throws IOException;
}
