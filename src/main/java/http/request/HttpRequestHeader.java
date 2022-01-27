package http.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRequestHeader {

    private Map<String, String> requestHeader = new HashMap<>();

    public void addHeader(String headerName, String headerContent) {
        requestHeader.put(headerName, headerContent);
    }

    public Optional<String> getHeader(String headerName) {
        return Optional.ofNullable(requestHeader.get(headerName));
    }
}
