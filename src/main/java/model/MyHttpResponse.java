package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MyHttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(MyHttpResponse.class);
    private static final String DEFAULT_PATH = "./webapp";
    private final String statusLine;
    private byte[] body;

    public MyHttpResponse(MyHttpRequest myHttpRequest) throws IOException {
        HttpStatus httpStatus = HttpStatus.OK;

        if (myHttpRequest.getMethod().equals("GET")) {
            try {
                body = Files.readAllBytes(new File(DEFAULT_PATH + myHttpRequest.getUri()).toPath());
            } catch (IOException e) {
                httpStatus = HttpStatus.NOT_FOUND;
            }
        }
        else if (myHttpRequest.getMethod().equals("POST")) {
            httpStatus = HttpStatus.CREATED;
        }

        statusLine = httpStatus.makeStatusLine(myHttpRequest.getProtocol());
    }

    public String getStatusLine() {
        return statusLine;
    }

    public byte[] getBody() {
        return body;
    }
}
