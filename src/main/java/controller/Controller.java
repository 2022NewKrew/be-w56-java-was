package controller;

import domain.HttpController;
import domain.HttpRequest;
import domain.RequestLine;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Controller {

    default HttpController init(BufferedReader bufferedReader, RequestLine requestLine) throws IOException {
        HttpRequest httpRequest = new HttpRequest(requestLine, HttpRequestUtils.parseHeaders(bufferedReader));
        String requestUrl = httpRequest.getRequestPath();
        String requestPath = HttpRequestUtils.getRequestPath(requestUrl);

        return new HttpController(httpRequest, requestUrl, requestPath);
    }

    void control(DataOutputStream dos, BufferedReader bufferedReader, RequestLine requestLine) throws IOException;
}
