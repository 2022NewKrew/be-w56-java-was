package webserver.http.request;

import java.util.HashMap;

public class HttpRequestHeader {
    private final HashMap<String, String> headers;

    public HttpRequestHeader(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }
}
