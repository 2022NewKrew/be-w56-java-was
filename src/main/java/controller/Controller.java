package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Controller {

    HttpResponse run(HttpRequest request, DataOutputStream dos)
            throws IOException;
}
