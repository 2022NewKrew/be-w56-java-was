package http.request;

import java.util.Map;

public class HttpHeaders {

    private final static String CONTENT_LENGTH = "Content-Length";

    private final Map<String, String> headers;

    public HttpHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getContentLength() {
        return Integer.parseInt(headers.getOrDefault(CONTENT_LENGTH, "0"));
    }
}
