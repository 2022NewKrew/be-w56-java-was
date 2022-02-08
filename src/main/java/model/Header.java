package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class Header {

    public static Header of(List<String> headerLineList) {
        Map<HttpHeader, String> header = new HashMap<>();

        for (String headerLine : headerLineList) {
            Pair parseHeaderResult = HttpRequestUtils.parseHeader(headerLine);
            header.put(HttpHeader.of(parseHeaderResult.getKey()), parseHeaderResult.getValue());
        }

        return new Header(header);
    }

    private final Map<HttpHeader, String> header;

    public Header(Map<HttpHeader, String> header) {
        this.header = Collections.unmodifiableMap(header);
    }

    public String get(String key) {
        return header.get(key);
    }

    public List<String> messageList() {
        List<String> messageList = new ArrayList<>();
        for (Entry<HttpHeader, String> entry : header.entrySet()) {
            HttpHeader key = entry.getKey();
            String value = entry.getValue();
            messageList.add(key + ": " + value);
        }
        return messageList;
    }

    @Override
    public String toString() {
        return "Header{" +
                "header=" + header +
                '}';
    }
}
