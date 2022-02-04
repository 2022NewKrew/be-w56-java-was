package http.response;

import http.MediaType;
import http.HttpStatus;
import http.HttpHeaders;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import webserver.ModelAndView;

public class HttpResponse {

    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String STATUS_LINE_DELIMITER = " ";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String LOCATION = "Location";
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String PATHNAME = "./webapp";

    private final HttpHeaders headers = new HttpHeaders(new HashMap<>());
    private HttpStatus httpStatus;
    private byte[] body;

    public HttpResponse() {
    }

    private void set(HttpStatus httpStatus) {
        set(httpStatus, new byte[0]);
    }

    private void set(HttpStatus httpStatus, byte[] body) {
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public void from(ModelAndView mv) throws IOException {
        if (mv.getStatus() == HttpStatus.OK) {
            ok(mv);
            return;
        }
        if (mv.getStatus() == HttpStatus.FOUND) {
            redirect(mv);
            return;
        }
        error(mv);
    }

    private void ok(ModelAndView mv) throws IOException {
        Path path = new File(PATHNAME + mv.getViewName()).toPath();
        if (Files.notExists(path)) {
            ModelAndView errorView = ModelAndView.error(HttpStatus.PAGE_NOT_FOUND);
            error(errorView);
        }

        byte[] body = Files.readAllBytes(path);
        MediaType contentType = MediaType.getMediaType(mv.getViewName());

        headers.put(CONTENT_TYPE, contentType.getType());
        headers.put(CONTENT_LENGTH, String.valueOf(body.length));

        set(mv.getStatus(), body);
    }

    private void error(ModelAndView mv) {
        error(mv.getStatus(), mv.getStatus().getErrorMessage());
    }

    public void error(HttpStatus status, String message) {
        byte[] body = message.getBytes(StandardCharsets.UTF_8);
        headers.put(CONTENT_LENGTH, String.valueOf(body.length));
        headers.put(CONTENT_TYPE, "text/plain; charset=utf-8");

        set(status, body);
    }

    private void redirect(ModelAndView mv) {
        headers.put(LOCATION, mv.getViewName());
        set(mv.getStatus());
    }

    public void setCookie(String cookie) {
        headers.put(SET_COOKIE, cookie);
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
