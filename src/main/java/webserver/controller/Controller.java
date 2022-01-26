package webserver.controller;

import util.request.HttpRequest;
import util.response.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Controller<T> {
    boolean supports(String url);
    HttpResponse<T> handle(HttpRequest httpRequest, DataOutputStream dos) throws IOException;
}
