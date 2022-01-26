package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class Header {

    public static Header of(List<String> headerLineList) {
        Map<String, String> header = new HashMap<>();

        for (String headerLine : headerLineList) {
            Pair parseHeaderResult = HttpRequestUtils.parseHeader(headerLine);
            header.put(parseHeaderResult.getKey(), parseHeaderResult.getValue());
        }

        return new Header(header);
    }

    private final Map<String, String> header;

    private Header(Map<String, String> header) {
        this.header = header;
    }

    public String get(String key) {
        return header.get(key);
    }

    @Override
    public String toString() {
        return "Header{" +
                "header=" + header +
                '}';
    }
}
