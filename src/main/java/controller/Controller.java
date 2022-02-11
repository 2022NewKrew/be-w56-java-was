package controller;

import httpmodel.HttpRequest;
import httpmodel.HttpResponse;
import java.io.IOException;

public interface Controller {

    void service(HttpRequest request, HttpResponse response) throws IOException;
}
