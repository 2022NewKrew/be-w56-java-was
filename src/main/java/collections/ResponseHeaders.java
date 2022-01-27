package collections;

import java.util.Map;
import java.util.Set;

public class ResponseHeaders {

    private Map<String, String> headers;

    public ResponseHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public Set<String> getHeaderKeys() {
        return headers.keySet();
    }
}
