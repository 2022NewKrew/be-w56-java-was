package webserver.controller;

import webserver.request.HttpRequest;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Controller {
    boolean supports(String url);
    void handle(HttpRequest httpRequest, DataOutputStream dos) throws IOException;

    default void response200Header(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    default void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
