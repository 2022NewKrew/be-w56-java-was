package http.request;

import http.MediaType;
import java.util.Map;

public class HttpHeaders {

    private final static String CONTENT_LENGTH = "Content-Length";
    private static final String CONTENT_TYPE = "Content-Type";

    private final Map<String, String> headers;

    public HttpHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getContentLength() {
        return Integer.parseInt(headers.getOrDefault(CONTENT_LENGTH, "0"));
    }

    public MediaType getContentType() {
        return MediaType.matchType(headers.getOrDefault(CONTENT_TYPE, "*/*"));
    }
}
