package http;

import util.HttpRequestUtils.Pair;

import java.util.HashMap;
import java.util.Map;

import static util.HttpRequestUtils.parseHeader;

public class HttpHeader {

    private final Map<String, String> header;

    public HttpHeader() {
        header = new HashMap<>();
    }

    public void addHeaderParameter(String headerLine) {
        Pair pair = parseHeader(headerLine);
        header.put(pair.getKey(), pair.getValue());
    }

    public int getContentLength() {
        return Integer.parseInt(header.getOrDefault("Content-Length", "0"));
    }

}
