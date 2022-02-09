package controller;

import java.io.IOException;
import model.HttpRequest;
import model.HttpResponse;

public interface Controller {

    HttpResponse run(HttpRequest request) throws IOException;
}
