package http;

import http.util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpHeaders {

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String LOCATION = "Location";

    private final Map<String, String> headers;

    private HttpHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public static HttpHeaders create(BufferedReader br) throws IOException {
        Map<String, String> headerMap = new HashMap<>();
        String line = null;
        while((line = br.readLine()) != null && !("".equals(line))) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headerMap.put(pair.getKey(), pair.getValue());
        }
        return new HttpHeaders(headerMap);
    }

    public static HttpHeaders of(Map<String, String> map) {
        return new HttpHeaders(map);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Optional<String> getHeader(String key) {
        return Optional.ofNullable(headers.get(key));
    }
}
