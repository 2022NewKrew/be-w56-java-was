package collections;

import java.util.Map;

public class ResponseHeaders {

    private Map<String, String> headers;

    public ResponseHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }
}
