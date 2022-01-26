package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewController {

    private static final Logger log;
    private static final ViewController INSTANCE;

    private static final String STATIC_FILE_BASE_DIRECTORY;

    static {
        log = LoggerFactory.getLogger(ViewController.class);
        INSTANCE = new ViewController();
        STATIC_FILE_BASE_DIRECTORY = "./webapp";
    }

    private ViewController() {}

    public static ViewController getInstance() {
        return INSTANCE;
    }

    public void viewResolve(String viewPath, DataOutputStream dos) throws IOException {
        byte[] body = Files.readAllBytes(new File(STATIC_FILE_BASE_DIRECTORY + viewPath).toPath());

        this.response200Header(dos, body.length);
        this.responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
//            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
