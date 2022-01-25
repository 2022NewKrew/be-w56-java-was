package webserver.http.common;

import webserver.http.response.HttpResponseBody;

import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {
    private static final String HTTP_HEADER_LINE_DELIMITER = "\r\n";
    private static final String HTTP_HEADER_KEY_VALUE_DELIMITER = ": ";

    private final Map<String, String> headers;

    public HttpHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public HttpHeaders() {
        this(new HashMap<>());
    }

    public static HttpHeaders from(HttpResponseBody body) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", body.getContentType() + ";charset=utf-8");
        headers.put("Content-Length", String.valueOf(body.getLength()));
        return new HttpHeaders(headers);
    }

    public String getHeader(String name) {
        if (headers.containsKey(name)) {
            return headers.get(name);
        }

        return null;
    }

    public String getFormattedHeader() {
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, value) -> sb.append(String.format("%s%s%s%s", key, HTTP_HEADER_KEY_VALUE_DELIMITER, value, HTTP_HEADER_LINE_DELIMITER)));
        return sb.toString();
    }

    @Override
    public String toString() {
        return "HttpHeaders{" +
                "headerMap=" + headers +
                '}';
    }

    public void parseHeaderLine(String headerLine) {
        String[] headerToken = headerLine.split(HTTP_HEADER_KEY_VALUE_DELIMITER);
        headers.put(headerToken[0], headerToken[1]);
    }
}
