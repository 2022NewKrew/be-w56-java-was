package http.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseHeader {

    private Map<String, String> responseHeader = new HashMap<>();

    public void addHeader(String headerName, String headerContent) {
        responseHeader.put(headerName, headerContent);
    }

    public Map<String, String> getHeaders() {
        return Map.copyOf(responseHeader);
    }

}
