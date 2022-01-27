package http.response;

import http.ContentType;
import http.HttpStatus;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String STATUS_LINE_DELIMITER = " ";
    private static final String HEADER_KEY_VALUE_DELIMITER = ": ";

    private final HttpStatus httpStatus;
    private final Map<String, String> headers;
    private final byte[] body;

    private HttpResponse(HttpStatus httpStatus, Map<String, String> header) {
        this(httpStatus, header, new byte[0]);
    }

    private HttpResponse(HttpStatus httpStatus, Map<String, String> headers, byte[] body) {
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public static HttpResponse ok(ContentType contentType, byte[] body) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", contentType.getType());
        return new HttpResponse(HttpStatus.OK, headers, body);
    }

    public static HttpResponse error(HttpStatus httpStatus) {
        Map<String, String> headers = new HashMap<>();
        byte[] body = httpStatus.getErrorMessage().getBytes(StandardCharsets.UTF_8);
        return new HttpResponse(httpStatus, headers, body);
    }

    public static HttpResponse redirect(String url) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", url);
        return new HttpResponse(HttpStatus.FOUND, headers);
    }

    public String getHeader() {
        StringBuilder sb = new StringBuilder();

        sb.append(HTTP_VERSION)
                .append(STATUS_LINE_DELIMITER)
                .append(httpStatus.getStatus())
                .append(System.lineSeparator());
        headers.forEach(
                (key, value) -> sb.append(key)
                        .append(HEADER_KEY_VALUE_DELIMITER)
                        .append(value)
                        .append(System.lineSeparator()));
        sb.append(System.lineSeparator());

        return sb.toString();
    }

    public byte[] getBody() {
        return body;
    }
}
