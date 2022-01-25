package webserver.controller;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.manage.RequestFile;
import webserver.RequestHandler;
import webserver.manage.RequestParser;

import java.io.*;

public class GetController implements MethodController {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final String SIGN_UP = "/user/create";

    RequestParser rp;
    OutputStream os;

    public GetController(RequestParser rp, OutputStream os) {
        this.rp = rp;
        this.os = os;
    }

    public void service() throws IOException {
        log.info(":: GET Service");

        switch (rp.getPath()) {
            case SIGN_UP:
                methodSignUp();
                break;
            default:
                methodDefault();
                break;
        }
    }

    private void methodSignUp () throws IOException {
        String userId = rp.getQuery("userId");
        String password = rp.getQuery("password");
        String name = rp.getQuery("name");
        String email = rp.getQuery("email");
        User user = new User(userId,password,name,email);

        log.info(user.toString());

        DataOutputStream dos = new DataOutputStream(os);

        RequestFile requestFile = new RequestFile(RequestFile.ERROR_FILE);
        byte[] body = requestFile.getFileBytes();

        response200Header(rp, dos, body.length);
        responseBody(dos, body);
    }

    private void methodDefault () throws IOException {
        DataOutputStream dos = new DataOutputStream(os);

        RequestFile requestFile = new RequestFile(rp.getPath());
        byte[] body = requestFile.getFileBytes();

        response200Header(rp, dos, body.length);
        responseBody(dos, body);
    }

    private void response200Header(RequestParser rp, DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+rp.getContentType()+";charset=utf-8\r\n");
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
