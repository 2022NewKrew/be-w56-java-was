package util;

import db.DataBase;
import model.RequestLine;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class RequestPathController {
    private static final Logger log = LoggerFactory.getLogger(RequestPathController.class);
    private static final String URL_PREFIX = "./webapp";
    private static final String DEFAULT_CONTENT_TYPE = "text/html";

    public static void urlMapping(RequestLine requestLine, String contentType, DataOutputStream dos) throws IOException {
        log.info("Request Url : {}", requestLine.getUrl());
        switch (requestLine.getUrl()) {
            case "/":
                rootPath(dos);
                break;
            case "/user/create":
                userCreatePath(requestLine, contentType, dos);
                break;
            default:
                defaultPath(requestLine, contentType, dos);
                break;
        }
    }

    private static void rootPath(DataOutputStream dos) {
        byte[] body = "Hello World".getBytes(StandardCharsets.UTF_8);
        responseHeader(ResponseStatus.OK, DEFAULT_CONTENT_TYPE, dos, body.length);
        responseBody(dos, body);
    }

    private static void userCreatePath(RequestLine requestLine, String contentType, DataOutputStream dos) {
        Map<String, String> userInput = HttpRequestUtils.parseQueryString(requestLine.getQuery());
        User user = new User(userInput.get("userId"), userInput.get("password"), userInput.get("name"), userInput.get("email"));
        DataBase.addUser(user);

        byte[] body = DataBase.findUserById(userInput.get("userId")).toString().getBytes(StandardCharsets.UTF_8);
        responseHeader(ResponseStatus.OK, contentType, dos, body.length);
        responseBody(dos, body);
    }

    private static void defaultPath(RequestLine requestLine, String contentType, DataOutputStream dos) throws IOException {
        File file = new File(URL_PREFIX + requestLine.getUrl());
        byte[] body;

        if (file.exists()) {
            body = Files.readAllBytes(file.toPath());
            responseHeader(ResponseStatus.OK, contentType, dos, body.length);
        } else {
            body = ResponseStatus.NOT_FOUND.name().getBytes(StandardCharsets.UTF_8);
            responseHeader(ResponseStatus.NOT_FOUND, contentType, dos, body.length);
        }
        responseBody(dos, body);
    }

    private static void responseHeader(ResponseStatus status, String contentType, DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 " + status.getValue() + " " + status.name() + " \r\n");
            dos.writeBytes("Content-Type: " + contentType + "; charset = utf - 8\r\n ");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
