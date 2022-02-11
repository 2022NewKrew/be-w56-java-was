package controller;

import httpmodel.HttpRequest;
import httpmodel.HttpResponse;
import java.io.IOException;

public abstract class AbstractController implements Controller {

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (httpRequest.getMethod().equals("GET")) {
            doGet(httpRequest, httpResponse);
        }
        if (httpRequest.getMethod().equals("POST")) {
            doPost(httpRequest, httpResponse);
        }
    }

    protected abstract void doGet(HttpRequest httpRequest, HttpResponse httpResponse)
        throws IOException;

    protected abstract void doPost(HttpRequest httpRequest, HttpResponse httpResponse)
        throws IOException;
}
