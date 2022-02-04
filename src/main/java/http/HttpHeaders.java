package http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpHeaders {

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String LOCATION = "Location";

    public static final String SET_COOKIE = "Set-Cookie";

    private final MultiValueMap<String, String> headers;

    private HttpHeaders(MultiValueMap<String, String> headers) {
        this.headers = headers;
    }

    public static HttpHeaders of(MultiValueMap<String, String> map) {
        return new HttpHeaders(map);
    }

    public MultiValueMap<String, String> getHeaders() {
        return headers;
    }

    public List<String> getAll(String key) {
        return headers.getAll(key);
    }

    public Optional<String> getFirst(String key) {
        return headers.getFirst(key);
    }

    public Integer getContentLength() {
        return Integer.valueOf(getFirst(HttpHeaders.CONTENT_LENGTH).orElse("0"));
    }
}
