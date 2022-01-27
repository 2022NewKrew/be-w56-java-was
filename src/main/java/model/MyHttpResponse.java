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
            httpStatus = post(myHttpRequest);
        }

        statusLine = httpStatus.makeStatusLine(myHttpRequest.getProtocol());
    }

    private HttpStatus get(MyHttpRequest request) {
        try {
            String uri = request.getUri();

            if (uri.equals("/")) {
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

    private HttpStatus post(MyHttpRequest request) {
        try {
            String uri = request.getUri();

            if (uri.equals("/user/create")) {
                userController.signUp(request.getParameters());

                request.setUri("/");
                body = Files.readAllBytes(new File(MAIN_PAGE).toPath());
                return HttpStatus.REDIRECT;
            }
            else {
                body = Files.readAllBytes(new File(DEFAULT_PATH + uri).toPath());
            }
            return HttpStatus.OK;
        } catch (IOException e) {
            return HttpStatus.NOT_FOUND;
        }
    }

    public String getStatusLine() {
        return statusLine;
    }

    public byte[] getBody() {
        return body;
    }
}
