package controller;

import model.RequestHeader;
import model.RequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ResponseStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import static controller.GeneralController.defaultPath;
import static controller.GeneralController.rootPath;
import static controller.UserController.*;

public class RequestPathMapper {
    private static final Logger log = LoggerFactory.getLogger(RequestPathMapper.class);

    protected static final String URL_PREFIX = "./webapp";
    protected static final String DEFAULT_CONTENT_TYPE = "text/html";
    protected static final String REDIRECT_PATH = "http://localhost:8080/index.html";

    public static void urlMapping(RequestLine requestLine, RequestHeader requestHeader,
                                  Map<String, String> requestBody, DataOutputStream dos) throws IOException {
        log.info("Request Url : {}", requestLine.getUrl());
        switch (requestLine.getUrl()) {
            case "/":
                rootPath(dos);
                break;
            case "/user":
                userCreatePath(requestHeader, requestBody, dos);
                break;
            default:
                defaultPath(requestLine, requestHeader, dos);
                break;
        }
    }

    protected static void responseHeader(ResponseStatus status, String contentType, DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 " + status.getValue() + " " + status.name() + " \r\n");
            dos.writeBytes("Content-Type: " + contentType + "; charset = utf - 8\r\n ");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    protected static void response302Header(String contentType, String redirectUrl, DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 " + ResponseStatus.FOUND.getValue() + " " + ResponseStatus.FOUND.name() + " \r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("Content-Type: " + contentType + "; charset = utf - 8\r\n ");
            dos.writeBytes("Content-Length: " + 0 + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    protected static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
