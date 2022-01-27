package webserver.response.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.response.ResponseCode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RedirectResponseFormat implements ResponseFormat {
    private static final Logger log = LoggerFactory.getLogger(RedirectResponseFormat.class);

    private static final String ERROR_PATH = "/error/error.html";

    private DataOutputStream dos;
    private String redirectPath;
    private String cookie;

    public RedirectResponseFormat(OutputStream os, String redirectPath) {
        this.dos = new DataOutputStream(os);
        this.redirectPath = redirectPath;
    }

    public void setCookie (String key, String value) {
        this.cookie = key+"="+value+"; Path=/";
    }

    @Override
    public void sendResponse (ResponseCode status) {
        switch (status) {
            case STATUS_302:
            case STATUS_303:
                responseHeader(status);
                break;
            case STATUS_303:
                response303Header();
                break;
            case STATUS_403:
            case STATUS_404:
                responseErrorHeader();
                break;
        }
        responseBody();
    }

    private void responseHeader(ResponseCode status) {
        try {
<<<<<<< HEAD:src/main/java/webserver/response/format/RedirectResponseFormat.java
            dos.writeBytes("HTTP/1.1 "+status.getStatusCode()+" "+status.getStatusName()+"\r\n");
=======
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
>>>>>>> 1798648 (3-4일차 PR (#190)):src/main/java/webserver/response/PostResponseFormat.java
            dos.writeBytes("Location: "+redirectPath+"\r\n");
            responseCookieHeader();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

<<<<<<< HEAD:src/main/java/webserver/response/format/RedirectResponseFormat.java
=======
    private void response303Header() {
        try {
            dos.writeBytes("HTTP/1.1 303 See Other\r\n");
            dos.writeBytes("Location: "+redirectPath+"\r\n");
            responseCookieHeader();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

>>>>>>> 1798648 (3-4일차 PR (#190)):src/main/java/webserver/response/PostResponseFormat.java
    private void responseCookieHeader() throws IOException {
        if(cookie != null && !"".equals(cookie.trim())) {
            dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
        }
    }

<<<<<<< HEAD:src/main/java/webserver/response/format/RedirectResponseFormat.java
    private void responseErrorHeader() {
=======
    private void responseError() {
>>>>>>> 1798648 (3-4일차 PR (#190)):src/main/java/webserver/response/PostResponseFormat.java
        try {
            dos.writeBytes("HTTP/1.1 303 See Other\r\n");
            dos.writeBytes("Location: "+ERROR_PATH+"\r\n");
            responseCookieHeader();
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
