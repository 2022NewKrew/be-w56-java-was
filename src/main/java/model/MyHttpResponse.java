package model;

import Controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MyHttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(MyHttpResponse.class);
    private static final String DEFAULT_PATH = "./webapp";
    private static final String MAIN_PAGE = "./webapp/index.html";
    private final String statusLine;
    private byte[] body;

    private final UserController userController = new UserController();

    public MyHttpResponse(MyHttpRequest myHttpRequest) throws IOException {
        HttpStatus httpStatus = HttpStatus.OK;

        if (myHttpRequest.getMethod().equals("GET")) {
            httpStatus = get(myHttpRequest);
        }
        else if (myHttpRequest.getMethod().equals("POST")) {
            httpStatus = HttpStatus.CREATED;
        }

        statusLine = httpStatus.makeStatusLine(myHttpRequest.getProtocol());
    }

    private HttpStatus get(MyHttpRequest request) {
        try {
            String uri = request.getUri();
            uri = processStaticUri(uri);

            if (uri.equals("/")) {
                body = Files.readAllBytes(new File(MAIN_PAGE).toPath());
            }
            else if (uri.equals("/user/create")) {
                userController.signUp(request.getParameters());
                body = Files.readAllBytes(new File(MAIN_PAGE).toPath());
            }
            else {
                body = Files.readAllBytes(new File(DEFAULT_PATH + uri).toPath());
            }
            return HttpStatus.OK;
        } catch (IOException e) {
            return HttpStatus.NOT_FOUND;
        }
    }

    private String processStaticUri(String uri) {
        String[] staticUri= {"/css", "/js", "/fonts", "/images"};
        for (String s : staticUri) {
            if (uri.contains(s)) {
                uri = uri.substring(1);
                int idx = uri.indexOf('/');
                return uri.substring(idx);
            }
        }
        return uri;
    }

    public String getStatusLine() {
        return statusLine;
    }

    public byte[] getBody() {
        return body;
    }
}
