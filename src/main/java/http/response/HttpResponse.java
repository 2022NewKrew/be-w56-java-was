package http.response;

import http.HttpStatus;
import http.HttpHeaders;
import java.util.HashMap;

public class HttpResponse {

    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String STATUS_LINE_DELIMITER = " ";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String LOCATION = "Location";
    private static final String SET_COOKIE = "Set-Cookie";

    private final HttpHeaders headers = new HttpHeaders(new HashMap<>());
    private HttpStatus httpStatus;
    private byte[] body;

    public HttpResponse() {
    }

    public void setCookie(String cookie) {
        headers.put(SET_COOKIE, cookie);
    }

    public void setLocation(String location) {
        headers.put(LOCATION, location);
    }

    public void setContentType(String contentType) {
        headers.put(CONTENT_TYPE, contentType);
    }

    public void setContentLength(String contentLength) {
        headers.put(CONTENT_LENGTH, contentLength);
    }

    public void setStatus(HttpStatus status) {
        this.httpStatus = status;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getHeader() {
        StringBuilder sb = new StringBuilder();

        sb.append(HTTP_VERSION)
                .append(STATUS_LINE_DELIMITER)
                .append(httpStatus.getStatus())
                .append(System.lineSeparator());
        sb.append(headers.getKeyValueString());
        sb.append(System.lineSeparator());

        return sb.toString();
    }

    public byte[] getBody() {
        return body;
    }
}
