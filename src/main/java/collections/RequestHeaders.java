package collections;

import java.util.Map;

public class RequestHeaders {

    private Map<String, String> headers;

    public RequestHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }
}
