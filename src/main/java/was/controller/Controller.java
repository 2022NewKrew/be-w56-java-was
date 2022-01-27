package was.controller;

import was.http.HttpRequest;
import was.http.HttpResponse;

import java.io.IOException;

public interface Controller {

    public void handle(HttpRequest request, HttpResponse response) throws IOException;

}
