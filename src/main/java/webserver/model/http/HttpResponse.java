package webserver.model.http;

import webserver.ViewRenderer;
import webserver.enums.HttpStatus;
import webserver.enums.MIME;
import webserver.model.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class HttpResponse {
    private static final String DEFAULT_URI = "http://localhost:8080";
    private static final String HTTP_VERSION = "HTTP//1.1";


    private String statusLine;
    private HttpStatus httpStatus;
    private String location;
    private byte[] body;
    private MIME mime;
    private List<Cookie> cookies = new ArrayList<>();

    public HttpResponse() {
    }

    public String getStatusLine() {
        return statusLine;
    }

    public void setStatusLine(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        statusLine = httpStatus.makeStatusLine(HTTP_VERSION);
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public HttpResponse addCookie(Cookie cookie) {
        cookies.add(cookie);
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

    public HttpResponse redirect(String location) {
        this.location = DEFAULT_URI + location;
        return this;
    }

    public HttpResponse setBody(ModelAndView modelAndView) {
        try {
            body = ViewRenderer.render(modelAndView);
        } catch (IOException e) {
            setStatusLine(HttpStatus.NOT_FOUND);
        }
        return this;
    }

    public String cookiesToString() {
        StringBuilder cookieHeader = new StringBuilder();
        for (Cookie cookie : cookies) {
            cookieHeader.append(cookie.toString());
            cookieHeader.append("\r\n");
        }
        return cookieHeader.toString();
    }
}
