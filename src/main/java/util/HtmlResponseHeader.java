package util;

import lombok.extern.slf4j.Slf4j;
import model.ResponseHeader;

import java.io.DataOutputStream;
import java.io.IOException;

@Slf4j
public enum HtmlResponseHeader {
    RESPONSE_200() {
        @Override
        public void response(DataOutputStream dos, ResponseHeader responseHeader) {
            try {
                dos.writeBytes("HTTP/1.1 200 OK \r\n");
                dos.writeBytes("Content-Type: " + responseHeader.getAccept() + ";charset=utf-8\r\n");
                dos.writeBytes("Content-Length: " + responseHeader.getLengthOfBodyContent() + "\r\n");
                dos.writeBytes("\r\n");
            } catch (IOException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
    },

    REDIRECT_302() {
        @Override
        public void response(DataOutputStream dos, ResponseHeader responseHeader) {
            try {
                log.info("302: " + responseHeader.getUri());
                dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
                dos.writeBytes("Location: " + responseHeader.getUri() + "\r\n");
                dos.writeBytes("\r\n");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    },

    REDIRECT_302_WITH_LOGIN_COOKIE() {
        @Override
        public void response(DataOutputStream dos, ResponseHeader responseHeader) {
            try {
                log.info("Login 302: " + responseHeader.getUri());
                dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
                dos.writeBytes("Location: " + responseHeader.getUri() + "\r\n");
                dos.writeBytes("Set-Cookie: logined=true; Path=/ \r\n");
                dos.writeBytes("\r\n");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    };

    HtmlResponseHeader() {

    }

    public abstract void response(DataOutputStream dos, ResponseHeader responseHeader);
}
