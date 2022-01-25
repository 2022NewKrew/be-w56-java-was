package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PostResponseFormat implements ResponseFormat {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final String ERROR_PATH = "/error/error.html";

    private DataOutputStream dos;
    private String redirectPath;

    public PostResponseFormat(OutputStream os, String redirectPath) {
        this.dos = new DataOutputStream(os);
        this.redirectPath = redirectPath;
    }

    @Override
    public void sendResponse (ResponseCode status) {
        switch (status) {
            case STATUS_302:
                response302Header();
                break;
            case STATUS_403:
            case STATUS_404:
            case STATUS_405:
                responseError();
                break;
        }
        responseBody();
    }

    private void response302Header() {
        try {
            dos.writeBytes("HTTP/1.1 303 See Other\r\n");
            dos.writeBytes("Location: "+redirectPath+"\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseError() {
        try {
            dos.writeBytes("HTTP/1.1 303 See Other\r\n");
            dos.writeBytes("Location: "+ERROR_PATH+"\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody() {
        try {
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
