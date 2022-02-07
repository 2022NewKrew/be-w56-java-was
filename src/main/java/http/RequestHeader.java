package http;

import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestHeader {
    private final Map<String, String> headers;

    public RequestHeader() {
        headers = new HashMap<>();
    }

    public void addLine(String line) {
        HttpRequestUtils.Pair header = HttpRequestUtils.parseHeader(line);
        headers.put(header.getKey(), header.getValue());
    }

    public int getContentLength() {
        return Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
