package webserver.model.http;

import webserver.enums.HttpStatus;
import webserver.enums.MIME;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class HttpResponse {
    private static final String DEFAULT_PATH = "./webapp";
    private static final String DEFAULT_URI = "http://localhost:8080";
    private static final String HTTP_VERSION = "HTTP//1.1";


    private String statusLine;
    private HttpStatus httpStatus;
    private String location;
    private byte[] body;
    private MIME mime;
    private Cookie cookie;

    public HttpResponse() {
    }

    public String getStatusLine() {
        return statusLine;
    }

    public void setStatusLine(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        statusLine = httpStatus.makeStatusLine(HTTP_VERSION);
    }

    public Cookie getCookie() {
        return cookie;
    }

    public HttpResponse setCookie(Cookie cookie) {
        this.cookie = cookie;
        return this;
    }

    public byte[] getBody() {
        if (body == null)
            return "".getBytes(StandardCharsets.UTF_8);
        return body;
    }

    public MIME getMime() {
        return mime;
    }

    public void setMime(MIME mime) {
        this.mime = mime;
    }

    public String getLocation() {
        return location;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }


    public static HttpResponse httpStatus(HttpStatus httpStatus) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusLine(httpStatus);
        return httpResponse;
    }

    public HttpResponse setView(String path) {
        try {
            body = Files.readAllBytes(new File(DEFAULT_PATH + path).toPath());
        } catch (IOException e) {
            setStatusLine(HttpStatus.NOT_FOUND);
        }
        return this;
    }

    public HttpResponse body(String body) {
        this.body = body.getBytes(StandardCharsets.UTF_8);
        return this;
    }

    public HttpResponse redirect(String location) {
        this.location = DEFAULT_URI + location;
        return this;
    }
}
