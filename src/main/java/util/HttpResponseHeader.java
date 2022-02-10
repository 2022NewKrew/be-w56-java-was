package util;

import lombok.extern.slf4j.Slf4j;
import model.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;

@Slf4j
public enum HttpResponseHeader {
    RESPONSE_200() {
        @Override
        public void response(DataOutputStream dos, HttpResponse responseHeader) {
            try {
                dos.writeBytes("HTTP/1.1 200 OK \r\n");
                dos.writeBytes("Content-Type: " + responseHeader.getAccept() + ";charset=utf-8\r\n");
                dos.writeBytes("Content-Length: " + responseHeader.getLengthOfBodyContent() + "\r\n");
                dos.writeBytes("Connection: keep-alive\r\n");
                dos.writeBytes("Keep-Alive: timeout=5; max=100\r\n");
                dos.writeBytes("\r\n");
            } catch (IOException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
    },

    REDIRECT_302() {
        @Override
        public void response(DataOutputStream dos, HttpResponse responseHeader) {
            try {
                log.info("302: " + responseHeader.getLocationUri());
                dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
                dos.writeBytes("Location: " + responseHeader.getLocationUri() + "\r\n");
                dos.writeBytes("Connection: keep-alive\r\n");
                dos.writeBytes("Keep-Alive: timeout=5; max=100\r\n");
                dos.writeBytes("\r\n");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    },

    REDIRECT_302_WITH_LOGIN_COOKIE() {
        @Override
        public void response(DataOutputStream dos, HttpResponse responseHeader) {
            try {
                log.info("Login 302: " + responseHeader.getLocationUri());
                dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
                dos.writeBytes("Location: " + responseHeader.getLocationUri() + "\r\n");
                dos.writeBytes("Connection: keep-alive\r\n");
                dos.writeBytes("Keep-Alive: timeout=5; max=100\r\n");
                dos.writeBytes("Set-Cookie: logined=true; Path=/ \r\n");
                dos.writeBytes("\r\n");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    },

    REDIRECT_302_WITH_LOGOUT_COOKIE() {
        @Override
        public void response(DataOutputStream dos, HttpResponse responseHeader) {
            try {
                log.info("Login 302: " + responseHeader.getLocationUri());
                dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
                dos.writeBytes("Location: " + responseHeader.getLocationUri() + "\r\n");
                dos.writeBytes("Connection: keep-alive\r\n");
                dos.writeBytes("Keep-Alive: timeout=5; max=100\r\n");
                dos.writeBytes("Set-Cookie: logined=false; Path=/ \r\n");
                dos.writeBytes("\r\n");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    };

    HttpResponseHeader() {

    }

    public abstract void response(DataOutputStream dos, HttpResponse responseHeader);
}
