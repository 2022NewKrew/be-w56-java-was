package http;

import http.util.HttpRequestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpHeader {
    private final Map<String, String> headers = new HashMap<>();

    public void addHeader(String line){
        if(Objects.isNull(line)) throw new IllegalArgumentException("null header");

        HttpRequestUtils.Pair header = parseHeader(line);

        headers.put(header.getKey(), header.getValue());
    }

    private HttpRequestUtils.Pair parseHeader(String header){
        return HttpRequestUtils.parseHeader(header);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }
}
