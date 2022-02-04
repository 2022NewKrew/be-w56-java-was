package http.response;

import http.MediaType;
import http.HttpStatus;
import http.request.HttpHeaders;
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
    private static final String PATHNAME = "./webapp";

    private final HttpStatus httpStatus;
    private final HttpHeaders headers;
    private final byte[] body;

    private HttpResponse(HttpStatus httpStatus, HttpHeaders header) {
        this(httpStatus, header, new byte[0]);
    }

    private HttpResponse(HttpStatus httpStatus, HttpHeaders headers, byte[] body) {
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public static HttpResponse from(ModelAndView mv) throws IOException {
        if (mv.getStatus() == HttpStatus.OK) {
            return ok(mv);
        }
        if (mv.getStatus() == HttpStatus.FOUND) {
            return redirect(mv);
        }
        return error(mv);
    }

    private static HttpResponse ok(ModelAndView mv) throws IOException {
        Path path = new File(PATHNAME + mv.getViewName()).toPath();
        byte[] body = Files.readAllBytes(path);
        MediaType contentType = MediaType.getMediaType(mv.getViewName());

        if (Files.notExists(path)) {
            ModelAndView errorView = ModelAndView.error(HttpStatus.PAGE_NOT_FOUND);
            return HttpResponse.error(errorView);
        }

        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, contentType.getType());
        headers.put(CONTENT_LENGTH, String.valueOf(body.length));

        return new HttpResponse(mv.getStatus(), new HttpHeaders(headers), body);
    }

    private static HttpResponse error(ModelAndView mv) {
        Map<String, String> headers = new HashMap<>();
        byte[] body = mv.getStatus().getErrorMessage().getBytes(StandardCharsets.UTF_8);
        headers.put(CONTENT_LENGTH, String.valueOf(body.length));

        return new HttpResponse(mv.getStatus(), new HttpHeaders(headers), body);
    }

    private static HttpResponse redirect(ModelAndView mv) {
        Map<String, String> headers = new HashMap<>();
        headers.put(LOCATION, mv.getViewName());

        return new HttpResponse(mv.getStatus(), new HttpHeaders(headers));
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
